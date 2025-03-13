# Tenugui

Tenugui is a ModalBottomSheet wrapper for Jetpack Compose.

## Why use Tenugui?

- compose-material, compose-material3 have different implementations about `BottomSheetDialog`
    - compose-material: Generic Compose implementation, need to consider Scrim, need to wrap with `ModalBottomSheetLayout`
    - compose-material3:1.1.X, 1.2.X -> Generic Compose implementation, draw on window
    - compose-material3:1.3.X -> ComposeView implementation, draw on ComponentDialog
- `compose-material` does not draw on the window, `compose-material3` has unstable APIs.
- You can use `BottomSheetDialog` with `Tenugui` without depending on `compose-material` or `compose-material3`.

- If you use compose material, but you want to use BottomSheetBehavior API, you can use `Tenugui`.
- If you use compose material3:**1.1.X**, **1.2.X**, you can use `Tenugui`.
- If you use compose material3:**1.3.X**, not need to use `Tenugui`. 👍

> [!NOTE]
> If compose-material starts to provide Popup BottomSheetDialog API, Tenugui finishes its role.

## Features

- `Tenugui` uses [BottomSheetDialog](https://developer.android.com/reference/com/google/android/material/bottomsheet/BottomSheetDialog) to show a modal bottom sheet.
- You can use BottomSheetBehavior API on Compose API.
- You can handle each cancel request, such as back key, outside touch.

## Usage

```kotlin
var visible by remember { mutableStateOf(false) }
if (visible) {
    val bottomSheetDialogState = rememberBottomSheetDialogState()
    BottomSheetDialog(
        onDismissRequest = {
            visible = false
        },
        state = bottomSheetDialogState,
    ) {
        Text("Text on BottomSheetDialog!!!")
    }
}
```

That's it! You can use BottomSheetDialog with Tenugui.

## Note

> [!IMPORTANT]
> PopupPositionProvider has issues about position. 
> It is recommended to use EditText with AndroidView if you care about the cursor handle position.
>
> - https://issuetracker.google.com/issues/265073970
> - https://issuetracker.google.com/issues/326394521
> - https://issuetracker.google.com/issues/391899135

## Installation

```gradle
implementation 'io.github.ryunen344.tenugui:tenugui:$version'
```

## License
```text
Copyright (C) 2017 The Android Open Source Project
Copyright (C) 2025 RyuNen344

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
```
