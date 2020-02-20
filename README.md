
# react-native-roam-rn-toolkit

## Getting started

`$ npm install react-native-roam-rn-toolkit --save`

### Mostly automatic installation

`$ react-native link react-native-roam-rn-toolkit`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-roam-rn-toolkit` and add `RNRoamRnToolkit.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNRoamRnToolkit.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNRoamRnToolkitPackage;` to the imports at the top of the file
  - Add `new RNRoamRnToolkitPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-roam-rn-toolkit'
  	project(':react-native-roam-rn-toolkit').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-roam-rn-toolkit/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-roam-rn-toolkit')
  	```


## Usage
```javascript
import RNRoamRnToolkit from 'react-native-roam-rn-toolkit';

// TODO: What to do with the module?
RNRoamRnToolkit;
```

## Keychain Example

This example fetches a value out of the keychain, then returns this if it succeeds.
If this fails, a UUID is generated set in the keychain with the same identifier.

If you run / close the app multiple times, the value returned should be consistent.

```javascript
import { Keychain } from 'roam-rn-toolkit'
import uuid from 'uuid'

async function getDeviceIdentifier() {
  let deviceIdentifier = await Keychain.getValue('deviceIdentifier').catch(
    () => {}
  )

  if (!deviceIdentifier) {
    deviceIdentifier = uuid.v4()
    await Keychain.setValue('deviceIdentifier', deviceIdentifier)
  }
  return deviceIdentifier
}
```

## Shadow example (Android only)

The `ShadowView` component provides a way of rendering a 9 patch shadow image on android.
To use `ShadowView` simply wrap the content you want to render the shadow under then add padding to reveal the intended amount of shadow.
Unfortunately this aproach is limted as the shadow itself actually takes up the space that it renders in unlike iOS.

### Props

#### color
The color prop takes in a color hex code and does a colour addition with the shadow image

#### insets
Adds padding to the shadow image without affecting the contents of the container in px.
An object with these supported properties:
 * top
 * bottom
 * left
 * right 

### example
```javascript
<ShadowView
    color="#FF0000"
    insets={{ top: 35 }}
    style={{ marginTop: 40, padding: 30 }}>
    <View style={styles.content}>
    ...        
    </View>
</ShadowView>
```