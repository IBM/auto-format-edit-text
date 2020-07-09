# Android Auto Format EditText
[![](https://jitpack.io/v/IBM/auto-format-edit-text.svg)](https://jitpack.io/#IBM/auto-format-edit-text)

A customizable solution for automatic text field formatting and masking in Android applications. The AutoFormatEditText component extends from AppCompatEditText and handles the automatic real-time formatting of content such as dates, credit card numbers, and phone numbers. For behavior more complex than can be achieved with AutoFormatEditText, you can create your own MaskingInputFilter.

## Example
```xml
<com.ibm.autoformatedittext.AutoFormatEditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="@{viewModel.formattedText}"
    app:onFormattedTextChanged="@{viewModel::onFormattedTextChanged}"
    app:onUnformattedValueChanged="@{viewModel::onUnformattedValueChanged}"
    app:inputMask="##/##/####"
    app:inputMaskPlaceholder="#" />
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

The following describes in more detail all custom attributes associated with FormattedInputEditText (extends AppCompatEditText):
* **onFormattedValueChanged** - Binding for FormattedInputEditText.FormattedTextChangedListener interface. Callback occurs when the formatted text (text visible to the user) changes value.  
* **onUnformattedValueChanged** - Binding for FormattedInputEditText.UnformattedValueListener interface. Callback occurs when the unformatted or "raw" form of the text changes value. For example, a date entered with the format ##/##/#### and the text 07/19/1993 would have an unformatted value of 07191993.
* **hideModeEnabled** - A boolean attribute specifying whether the edit text should hide its value and instead show a formatted replacement text. This is most often user for masking partial data that should not be edited by the user. Extend the FormattedInputEditText class to define the formatting behavior.

The following describes in more detail all custom attributes associated with AutoFormatEditText (extends FormattedInputEditText):
* **inputMask** - A template for the format of text inputed into the edit text. Slots for a user's input characters are denoted by a placeholder. All other characters are literal and are inserted as the text changes. For example, a date format might be ##/##/####
* **inputMaskPlaceholder** - An optional attribute specifying the character used by the input mask to represent the user's input characters (default is #). This attribute does not support data binding.
* **shiftModeEnabled** - An optional boolean attribute specifying the manner in which the edit text responds to backspacing while the cursor is in front characters that are mandated by the input mask. When shiftModeEnabled is true, the cursor will shift to the left until reaching a character that can be deleted (a character entered by the user), or until the cursor reaches the beginning of the edit text. When shiftModeEnabled is false, the cursor will simply shift to the left and disregard characters mandated by the mask. This attribute is false by default.
* **hideModeFormat** - An optional attribute for a format that determines the appearance of text while in hide mode. Individual characters of the unformatted text can be referenced by index, such as [n] for a single index or [n-m] for a range of indicies.  All other characters that do not reference indicies are literal. For example, if hideModeEnabled is true, then an unformatted value of 07191993 with a hideModeFormat of "\*\*\*[4][5][6][7]" would display as "\*\*\*1993". This would be equivalent to the shorthand format "\*\*\*[4-7]".

AutoFormatEditText accepts a mask string that defines the behavior of text as entered into the edit text. However, you can also define your own MaskingInputFilter on any FormattedEditText. For example:

```{java}
myFormattedEditText.setMaskingInputFilter(
    new FormattedEditText.MaskingInputFilter() {
	@Override
	public EditTextState filter(String textBefore, String textAfter, 
		int selectionStart, int selectionLength, int replacementLength) {
		...
	}
    });
```

<!-- License and Authors is optional here, but gives you the ability to highlight who is involed in the project -->
## License & Authors

If you would like to see the detailed LICENSE click [here](LICENSE).

- Author: Carl Montgomery <carl.montgomery@ibm.com> or <carljmont@gmail.com>

```text
Copyright:: 2020- IBM, Inc

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
