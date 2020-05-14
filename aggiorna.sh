scp ./ConvertitoreCoordinate/build/outputs/apk/ConvertitoreCoordinate-debug.apk leonardo@192.168.1.105:/home/leonardo 
scp ./ConvertitoreCoordinateAds/build/outputs/apk/ConvertitoreCoordinateAds-debug.apk leonardo@192.168.1.105:/home/leonardo
ssh leonardo@192.168.1.105 adb install -r ConvertitoreCoordinate-debug.apk
ssh leonardo@192.168.1.105 adb install -r ConvertitoreCoordinateAds-debug.apk
