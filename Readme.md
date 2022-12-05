<h1 align="center">Compose Currency Text</h1>
 <p align="center">

   <a href="https://jitpack.io/#Muratthekus/Compose-CurrencyText"> <img src="https://jitpack.io/v/Muratthekus/Compose-CurrencyText.svg" /></a>
   <br />
     Outlined text field specialied for curreny inputs. Decimal formatter supports different locales.
 </p>

<img width="301" height="110" alt="sample_image_1" src="https://user-images.githubusercontent.com/45212967/205649696-f90445cb-0491-44d5-8be9-d27643a8062f.png">
<img width="299"  height="110" alt="sample_image_2" src="https://user-images.githubusercontent.com/45212967/205650569-309533f4-f73f-4095-b221-082913cf18a1.png">


## How to integrate into your app?
Inorder to use CurrencyText add JitPack repository to root level build.gradle file.

```gradle
    repositories {
        mavenCentral()
        google()
        maven { url "https://jitpack.io" }
    }
```

And add CurrencyText to your dependencies

```gradle
    implementation 'com.github.Muratthekus:Compose-CurrencyText:1.0'
```

## Usage
- You can use CurrencyText directly in your compose view.
 ```kotlin
    CurrencyTextField(
        onChange = {
            Log.d("new value", it)
        },
        currencySymbol = "$",
        limit = 100,
        errorColor = Color.Red,
        locale = Locale.getDefault(),
        initialText = "100",
        maxNoOfDecimal = 2,
        errorText = "Sample error text",
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
 ```

- Also you can use it inside of
  Add a compose view in your xml file like
 ```xml
 <androidx.compose.ui.platform.ComposeView
     android:id="@+id/currencyText"
     android:layout_width="match_parent"
     android:layout_height="match_parent" />
 ```



 ```kotlin

binding.currencyText.apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
    // In Compose world
        MaterialTheme {
            CurrencyTextField(
                onChange = {
                Log.d("new value", it)
            },
            currencySymbol = "$",
            limit = 100,
            errorColor = Color.Red,
            locale = Locale.getDefault(),
            initialText = "100",
            maxNoOfDecimal = 2,
            errorText = "Sample error text",
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        }
    }
}
 ```
## License
 ```
MIT License
Copyright (c) 2018 Mukesh Solanki
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
