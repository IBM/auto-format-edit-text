# Android Auto Format EditText
[![](https://jitpack.io/v/IBM/auto-format-edit-text.svg)](https://jitpack.io/#IBM/auto-format-edit-text)

A customizable solution for automatic text field formatting and masking in Android applications. The AutoFormatEditText component extends from AppCompatEditText and handles the automatic real-time formatting of content such as dates, credit card numbers, and phone numbers. An abstract class AbstractAutoEditText can be extended more fine grained control over the behavior.

## Example
```xml
<com.ibm.autoformatedittext.AutoFormatEditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="@{viewModel.formattedText}"
    app:onTextChanged="@{viewModel::onTextChanged}"
    app:onUnformattedValueChanged="@{viewModel::onUnformattedValueChanged}"
    app:format="##/##/####"
    app:placeholder="#" />
```
## Installation
Step 1: Add the JitPack repository to your project build.gradle file 
```gradle
allprojects {
  repositories {
    ...
	maven { url 'https://jitpack.io' }
  }
}
```

Step 2: Add the desired version as a dependency in your app build.gradle file (see badge for the latest stable version)
```gradle
dependencies {
  implementation 'com.github.IBM:auto-format-edit-text:1.0.2-beta'
}
```

## Usage

The following describes in more detail all custom attributes associated with AbstractAutoEditText (extends AppCompatEditText):
* **onTextChanged** - Binding for AbstractAutoEditText.TextChangedListener interface. Callback occurs when the "formatted text" changes.  
* **onUnformattedValueChanged** - Binding for AbstractAutoEditText.UnformattedValueListener interface. Callback occurs when the unformatted or "raw" form of the text changes. For example, a date entered with the format ##/##/#### and the text 07/19/1993 would have an unformatted value of 07191993.

The following describes in more detail all custom attributes associated with AutoFormatEditText (extends AbstractAutoEditText):
* **format** - A template for the formatted text. Slots for a user's input characters are denoted by a placeholder. All other characters are literal and are inserted as the text changes. For example, a date format might be ##/##/####
* **placeholder** - An optional attribute specifying the character used by the text format to represent the user's input characters (default is #). This attribute does not support data binding.


<!-- License and Authors is optional here, but gives you the ability to highlight who is involed in the project -->
## License & Authors

If you would like to see the detailed LICENSE click [here](LICENSE).

- Author: Carl Montgomery <carl.montgomery@ibm.com> or <carljmont@gmail.com>

```text
Copyright:: 2019- IBM, Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
