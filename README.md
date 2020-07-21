#Android BiometricApp

Setting Up Biometric Authentication in Android


- Integration
Biometric library integration is simple. We need to add the following line under the dependencies tag in build.gradle at the app level. Have a look:
dependencies {
    implementation 'androidx.biometric:biometric:1.0.1'
}


- Check Availability
With the BiometricManager instance, we need to access the canAuthenticate() public function, which results in an integer.
canAuthenticate() has four possible outcomes:
BIOMETRIC_SUCCESS: The device can work with biometric authentication.
BIOMETRIC_ERROR_NO_HARDWARE: No biometric features are available on this device.
BIOMETRIC_ERROR_HW_UNAVAILABLE: Biometric features are currently unavailable in the device.
BIOMETRIC_ERROR_NONE_ENROLLED: The user hasn’t associated any biometric credentials in the device yet.


- Setup biometric prompt
It’s time to post necessary information to the prompt through PromptInfo builder then show the biometric dialog using authenticate on 
the BiometricPrompt instance by passing promptInfo as a parameter.
That’s all; the rest of the work will be taken care of by the biometric API and will provide the appropriate result through callbacks.



<img src="https://github.com/demimola24/BiometricApp/blob/master/screen1.png" width="300" height="530">

<img src="https://github.com/demimola24/BiometricApp/blob/master/screen2.png" width="300" height="530">

<img src="https://github.com/demimola24/BiometricApp/blob/master/screen3.png" width="300" height="530">
