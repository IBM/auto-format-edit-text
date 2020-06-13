# Android Auto Format EditText
A customizable solution for automatic text field formatting and masking in Android applications. The AutoFormatEditText component extends from AppCompatEditText and handles the automatic real-time formatting of content such as dates, credit card numbers, and phone numbers. An abstract class AbstractAutoEditText can be extended more fine grained control over the behavior.

## Example
```xml
<com.ibm.autoformatedittext.AutoFormatEditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="@{viewModel.formattedText}"
    app:format="(###) ###-####"
    app:rawValue="@={viewModel.unformattedText}"
    app:placeholder="#" />
```

## Description

The following describes in more detail all custom attributes used by AutoFormatEditText:
* **text** - The formatted string content of the edit text
* **format** - Used to derive the formatted string. The places for a user's input characters are denoted by a placeholder character (default is #). All other characters are literal and are inserted as the text changes. For example, a date format might be ##/##/####
* **rawValue** - The unformatted value of the data entered into the text field. It can also be thought of the actual characters that the user types. For example, a date entered with the format ##/##/#### and the text 07/19/1993 would have a raw value of 07191993. This is also called the "unformatted text"
* **placeholder** - An optional attribute specifying the character used by the text format to represent the user's inputed characters. This character MAY be inputted by the user without side effects

| Attribute name  | Data binding support | Two-way data binding |
| -------------   | -------------------- | -------------------- |
| text            | yes                  | no                   |
| format          | yes                  | no                   |
| rawValue        | yes                  | yes                  |
| placeholder     | no                   | no                   |

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
