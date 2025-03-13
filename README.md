# Tenugui

Tenugui is ModalBottomSheet Wrapper for Jetpack Compose.

## Why use Tenugui?

- compose-material, compose-material3のBottomSheetDialog実装はバリエーションがある
  - compose-material: GenericなComposeとして実装されている Scrimを考慮する必要矢`ModalBottomSheetLayout`でラップする必要がある
  - compose-material3:1.1.X, 1.2.X -> GenericなComposeをwindowに描画している
  - compose-material3:1.3.X -> ComponentDialogにComposeViewで描画している

- compose-materialはwindowに描画されていない、compose-material3はAPIが安定していない問題がある
- Tenuguiを使用するとcompose-material, compose-material3に依存せずにBottomSheetDialogを使用できる


- If you use compose material, but you want to use BottomSheetBehavior API, you can use Tenugui.
- If you use compose material3:1.1.X, 1.2.X, you can use Tenugui.
- If you use compose material3:1.3.X, not need to use Tenugui.

> [!NOTE]
> If compose-material starts to provide Popup BottomSheetDialog API, Tenugui finishes its role.

## Features

- Tenugui uses [BottomSheetDialog](https://developer.android.com/reference/com/google/android/material/bottomsheet/BottomSheetDialog) to show a modal bottom sheet.
- Tenugui can handle [BottomSheetBehavior](https://developer.android.com/reference/com/google/android/material/bottomsheet/BottomSheetBehavior) on Compose API.
- Tenugui can handle each cancel request, such as back key, outside touch.

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
> TextFieldのcursor handleがズレて表示される問題を気にする場合はEditTextをAndroidViewで使用することをお勧めします。
>
> https://issuetracker.google.com/issues/265073970
> https://issuetracker.google.com/issues/326394521
> https://issuetracker.google.com/issues/391899135

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
