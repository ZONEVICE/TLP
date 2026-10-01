fn main() {
    // The widgets are custom (ui/metal.slint); the light style only guarantees that nothing
    // follows the desktop's dark theme.
    let config = slint_build::CompilerConfiguration::new().with_style("fluent-light".into());
    slint_build::compile_with_config("ui/app.slint", config)
        .expect("failed to compile the Slint UI");
}
