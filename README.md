# Tenugui

Tenugui is a ModalBottomSheet wrapper for Jetpack Compose.

## Why use Tenugui?

Handling `BottomSheetDialog` in Jetpack Compose can be tricky because:

- `compose-material` and `compose-material3` have different implementations:
  - `compose-material`: A generic Compose implementation that requires handling scrims and wrapping with ModalBottomSheetLayout.
  - `compose-material3`:
    - 1.1.X, 1.2.X: Generic Compose implementation, drawn on the window. 
    - 1.3.X and later: Uses `ComposeView`, drawn on `ComponentDialog`.
- `compose-material` does not draw on the window, while `compose-material3` has unstable APIs
- `Tenugui` allows you to use `BottomSheetDialog` without depending on `compose-material` or `compose-material3`.


## When should you use Tenugui?

- If you're using `compose-material` but want to leverage the BottomSheetBehavior API.
- If you're using `compose-material3` 1.1.X or 1.2.X.
- If you're using `compose-material3` 1.3.X or later, you do not need Tenugui. 👍

> [!NOTE]
> If `compose-material` provides a built-in `BottomSheetDialog` API in the future, `Tenugui` will no longer be necessary.

## Features

- Uses [BottomSheetDialog](https://developer.android.com/reference/com/google/android/material/bottomsheet/BottomSheetDialog) to show a modal bottom sheet.
- Supports [BottomSheetBehavior](https://developer.android.com/reference/com/google/android/material/bottomsheet/BottomSheetBehavior) API in Compose.
- Handles various dismiss interactions, such as back key presses and outside touches.

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

That's it! You can now use `BottomSheetDialog` with `Tenugui`.

## Known Issues

> [!IMPORTANT]
> PopupPositionProvider has positioning issues.
> If cursor handle positioning is important, it's recommended to use `EditText` with `AndroidView`.
>
> - [Google Issue Tracker #265073970](https://issuetracker.google.com/issues/265073970)
> - [Google Issue Tracker #326394521](https://issuetracker.google.com/issues/326394521)
> - [Google Issue Tracker #391899135](https://issuetracker.google.com/issues/391899135)

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
