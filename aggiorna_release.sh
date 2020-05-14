scp ./ConvertitoreCoordinate/build/outputs/apk/ConvertitoreCoordinate-release.apk leonardo@192.168.1.105:/home/leonardo 
scp ./ConvertitoreCoordinateAds/build/outputs/apk/ConvertitoreCoordinateAds-release.apk leonardo@192.168.1.105:/home/leonardo
ssh leonardo@192.168.1.105 adb install -r ConvertitoreCoordinate-release.apk
ssh leonardo@192.168.1.105 adb install -r ConvertitoreCoordinateAds-release.apk
