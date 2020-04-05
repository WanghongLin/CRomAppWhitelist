CRomAppWhitelist
======
[ ![Download](https://api.bintray.com/packages/wanghonglin/maven/crom-app-whitelist/images/download.svg?version=0.1.0) ](https://bintray.com/wanghonglin/maven/crom-app-whitelist/0.1.0/link)

Quick setting to add your app to whitelist for Chinese Android ROM

Motivation behind this project
------

Because the open of Android ecosystem and the absence of Google services in Chinese market, the Android ROM manufatured by Chinese company has a big different with the others in other part of the world.

They are deeply customized Android, when your application run on this devices, you barely know what strange behavoirs will bring to your app. These restriction can let some of the features of your app are unfunctional, it's unfriendly from the users perspectives. Here I give several examples

* app is been kill after screen off, in order to save battery
* app don't allow notification before Android 6.0
* app's service with `Service.START_STICKY` not restart after the service kill

If your app is not covering thousands and millions of users, like `Wechat`, your app is not on the whiltelist of the phone manufacture. So the only way is telling the user and guide them to add your app to the whiltelist, if your app's main features rely on some features which are disable by the ROMs.

How to use
------

This library has been published to jCenter, just apply following dependency in your gradle file

```gradle
implementation 'com.wanghong.cromappwhitelist:crom-app-whitelist:0.1.0'
```

Then the following code will navigate you the the specific whitelist feature settings.

```java
   AppWhitelist.settingForAutoStart(this);
   AppWhitelist.settingForBatterySaver(this);
   AppWhitelist.settingForMemoryAcceleration(this);
   AppWhitelist.settingForNotification(this);
```
Will guide the user (with a `Intent` to start the `Activity`) to setting and add to the whiltelist if the device has these features.

The component name of these activities usually obtained with the following command which run in the device shell

```sh
$ adb shell dumpsys activity activities |grep Focused
```

Currently tested devices

* Xiaomi
* Meizu
* Samsung
* Oppo
* Huawei

Patches for more devices are welcome!

License
------
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
