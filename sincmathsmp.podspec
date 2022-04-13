Pod::Spec.new do |spec|
    spec.name                     = 'sincmathsmp'
    spec.version                  = '0.2.2'
    spec.homepage                 = 'https://github.com/GallVp/SincMathsMP'
    spec.source                   = { :http=> ''}
    spec.authors                  = 'Usman Rashid'
    spec.license                  = ''
    spec.summary                  = 'SincMaths Library'
    spec.vendored_frameworks      = 'build/cocoapods/framework/SincMaths.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '13.0'
                
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':',
        'PRODUCT_MODULE_NAME' => 'SincMaths',
    }
                
    spec.script_phases = [
        {
            :name => 'Build sincmathsmp',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$COCOAPODS_SKIP_KOTLIN_BUILD" ]; then
                  echo "Skipping Gradle build task invocation due to COCOAPODS_SKIP_KOTLIN_BUILD environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end