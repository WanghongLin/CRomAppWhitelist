CRomAppWhitelist
================
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.wanghonglin/crom-app-whitelist/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.wanghonglin/crom-app-whitelist)

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
implementation 'io.github.wanghonglin:crom-app-whitelist:0.1.2'
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
$ adb shell dumpsys activity activities |grep 'Focused\|ResumedActivity'
```

Currently tested devices

* Xiaomi
* Meizu
* Samsung
* Oppo
* Huawei

Patches for more devices are welcome!

More ROM or devices support
---------------------------
You can add more settings support new rom or devices, just add `appwhitelist.json` file into your app's asset root directory.

The library will read and resolve `appwhitelist.json` which have higher priority to launch setting intent.

Below is an example

```json
{
  "auto_start": [{
    "pkg": "com.huawei.systemmanager",
    "cls": "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"
  }, {
    "pkg": "com.huawei.xxx",
    "cls": "com.huawei.xxx.XActivity"
  }],
  "battery_saver": [],
  "mem_acc": [],
  "notification": []
}
```
* A functionality for different ROMs grouped together
* There are four different functionality settings, include the functionality you want to add as main key
    * `auto_start` for auto start settings
    * `battery_saver` for battery saver settings
    * `mem_acc` for memory acceleration settings
    * `notification` for notification settings
* In order to support multiple roms or manufactures, each functionality include a list of component for settings, the library will resolve them
from begin to end until a resolvable intent is found, then the library will `startActivity` with
this resolved intent and lead the user to settings page


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
