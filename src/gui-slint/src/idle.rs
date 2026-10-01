//! Mouse idle watcher: applies a value while the pointer stays still and restores the manual
//! value when it moves again, like WatcherMouseAction.java.

use std::error::Error;
use std::time::{Duration, Instant};

use x11rb::connection::Connection;
use x11rb::protocol::xproto::{ConnectionExt, Window};
use x11rb::rust_connection::RustConnection;

use crate::tlp::Mode;

pub type Position = (i16, i16);

#[derive(Debug, PartialEq, Eq)]
pub enum IdleEvent {
    /// The pointer stayed still long enough: set `value` for `mode`.
    Idle { mode: Mode, value: i32 },
    /// The pointer moved after `Idle`: restore the manual value of `mode`.
    Moved { mode: Mode },
}

pub struct IdleWatcher {
    value: i32,
    idle_after: Duration,
    last_position: Position,
    last_move: Instant,
    /// Mode whose value the watcher replaced and has to restore.
    applied: Option<Mode>,
}

impl IdleWatcher {
    pub fn new(value: i32, idle_after: Duration, position: Position, now: Instant) -> Self {
        Self {
            value,
            idle_after,
            last_position: position,
            last_move: now,
            applied: None,
        }
    }

    pub fn applied(&self) -> Option<Mode> {
        self.applied
    }

    /// `mode` is the GUI's current mode: the idle value is applied to it. The restore goes to
    /// the mode that got the idle value, even if the GUI's mode was toggled in between.
    pub fn poll(&mut self, position: Position, now: Instant, mode: Mode) -> Option<IdleEvent> {
        if position != self.last_position {
            self.last_position = position;
            self.last_move = now;
            return self.applied.take().map(|mode| IdleEvent::Moved { mode });
        }
        if self.applied.is_none() && now.duration_since(self.last_move) >= self.idle_after {
            self.applied = Some(mode);
            return Some(IdleEvent::Idle {
                mode,
                value: self.value,
            });
        }
        None
    }

    /// A value was set by hand for `mode` (big button or timer): it replaces the idle value of
    /// that mode, and it counts as activity, so the idle value only comes back after a new
    /// idle period.
    pub fn manual_change(&mut self, mode: Mode, now: Instant) {
        if self.applied == Some(mode) {
            self.applied = None;
        }
        self.last_move = now;
    }
}

/// Global pointer position, through an X11 connection of its own.
pub struct Pointer {
    connection: RustConnection,
    root: Window,
}

impl Pointer {
    pub fn connect() -> Result<Self, Box<dyn Error>> {
        let (connection, screen) = x11rb::connect(None)?;
        let root = connection.setup().roots[screen].root;
        Ok(Self { connection, root })
    }

    pub fn position(&self) -> Result<Position, Box<dyn Error>> {
        let reply = self.connection.query_pointer(self.root)?.reply()?;
        Ok((reply.root_x, reply.root_y))
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    const SECOND: Duration = Duration::from_secs(1);

    fn watcher(start: Instant) -> IdleWatcher {
        IdleWatcher::new(8, 10 * SECOND, (100, 100), start)
    }

    #[test]
    fn applies_once_after_idle_time_and_restores_on_move() {
        let t0 = Instant::now();
        let mut w = watcher(t0);
        assert_eq!(w.poll((100, 100), t0 + 9 * SECOND, Mode::Bat), None);
        assert_eq!(
            w.poll((100, 100), t0 + 10 * SECOND, Mode::Bat),
            Some(IdleEvent::Idle {
                mode: Mode::Bat,
                value: 8
            })
        );
        assert_eq!(w.poll((100, 100), t0 + 30 * SECOND, Mode::Bat), None);
        assert_eq!(
            w.poll((101, 100), t0 + 31 * SECOND, Mode::Bat),
            Some(IdleEvent::Moved { mode: Mode::Bat })
        );
        assert_eq!(w.poll((102, 100), t0 + 32 * SECOND, Mode::Bat), None);
        assert_eq!(w.applied(), None);
    }

    #[test]
    fn movement_restarts_the_idle_period() {
        let t0 = Instant::now();
        let mut w = watcher(t0);
        assert_eq!(w.poll((5, 5), t0 + 8 * SECOND, Mode::Bat), None);
        assert_eq!(w.poll((5, 5), t0 + 17 * SECOND, Mode::Bat), None);
        assert!(w.poll((5, 5), t0 + 18 * SECOND, Mode::Bat).is_some());
    }

    #[test]
    fn uses_the_current_mode_and_restores_the_mode_it_changed() {
        let t0 = Instant::now();
        let mut w = watcher(t0);
        assert_eq!(
            w.poll((100, 100), t0 + 10 * SECOND, Mode::Ac),
            Some(IdleEvent::Idle {
                mode: Mode::Ac,
                value: 8
            })
        );
        // Mode toggled to battery (keyboard or touch) before the pointer moves.
        assert_eq!(
            w.poll((0, 0), t0 + 11 * SECOND, Mode::Bat),
            Some(IdleEvent::Moved { mode: Mode::Ac })
        );
        assert_eq!(
            w.poll((0, 0), t0 + 21 * SECOND, Mode::Bat),
            Some(IdleEvent::Idle {
                mode: Mode::Bat,
                value: 8
            })
        );
    }

    #[test]
    fn manual_change_replaces_the_idle_value_and_counts_as_activity() {
        let t0 = Instant::now();
        let mut w = watcher(t0);
        assert!(w.poll((100, 100), t0 + 10 * SECOND, Mode::Bat).is_some());
        w.manual_change(Mode::Bat, t0 + 12 * SECOND);
        assert_eq!(w.applied(), None);
        assert_eq!(w.poll((100, 100), t0 + 21 * SECOND, Mode::Bat), None);
        assert_eq!(
            w.poll((100, 100), t0 + 22 * SECOND, Mode::Bat),
            Some(IdleEvent::Idle {
                mode: Mode::Bat,
                value: 8
            })
        );
    }

    #[test]
    fn manual_change_in_the_other_mode_keeps_the_pending_restore() {
        let t0 = Instant::now();
        let mut w = watcher(t0);
        assert!(w.poll((100, 100), t0 + 10 * SECOND, Mode::Bat).is_some());
        w.manual_change(Mode::Ac, t0 + 12 * SECOND);
        assert_eq!(w.applied(), Some(Mode::Bat));
        assert_eq!(
            w.poll((1, 1), t0 + 13 * SECOND, Mode::Ac),
            Some(IdleEvent::Moved { mode: Mode::Bat })
        );
    }
}
