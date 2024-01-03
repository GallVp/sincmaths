Pod::Spec.new do |spec|
    spec.name                     = 'SincMaths'
    spec.version                  = '0.3'
    spec.homepage                 = 'https://github.com/GallVp/sincmaths'
    spec.source                   = { :http=> ''}
    spec.authors                  = 'Usman Rashid'
    spec.license                  = ''
    spec.summary                  = 'SincMaths: A Kotlin multi-platform implementation of 2D matrix with signal processing capabilities'
    spec.vendored_frameworks      = 'build/cocoapods/framework/SincMaths.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '13.0'
                
                
    if !Dir.exist?('build/cocoapods/framework/SincMaths.framework') || Dir.empty?('build/cocoapods/framework/SincMaths.framework')
        raise "

        Kotlin framework 'SincMaths' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :sincmathsmp:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':sincmathsmp',
        'PRODUCT_MODULE_NAME' => 'SincMaths',
    }
                
    spec.script_phases = [
        {
            :name => 'Build SincMaths',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end