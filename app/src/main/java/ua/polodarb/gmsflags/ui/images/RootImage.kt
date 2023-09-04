package ua.polodarb.gmsflags.ui.images

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
@Suppress("BooleanLiteralArgument")
fun getGMSImagesRoot(colorScheme: ColorScheme): ImageVector {
    return remember {
        Builder(
            name = "RootImage", defaultWidth = 373.0.dp, defaultHeight = 373.0.dp,
            viewportWidth = 373.0f, viewportHeight = 373.0f
        ).apply {
            path(
                fill = SolidColor(colorScheme.surfaceVariant), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(27.64f, 178.349f)
                curveTo(31.358f, 209.837f, 45.735f, 246.447f, 74.449f, 263.266f)
                curveTo(117.834f, 288.682f, 114.202f, 327.775f, 198.884f, 344.769f)
                curveTo(212.086f, 347.421f, 224.616f, 348.536f, 236.431f, 348.316f)
                curveTo(242.783f, 348.198f, 248.93f, 347.686f, 254.86f, 346.813f)
                curveTo(308.68f, 338.951f, 344.898f, 301.541f, 358.187f, 256.382f)
                curveTo(375.673f, 196.991f, 340.757f, 158.108f, 317.614f, 134.681f)
                curveTo(294.47f, 111.254f, 277.519f, 38.259f, 194.403f, 20.898f)
                curveTo(181.73f, 18.256f, 168.77f, 16.908f, 155.814f, 17.232f)
                curveTo(126.242f, 17.958f, 93.985f, 27.669f, 71.667f, 47.936f)
                curveTo(69.991f, 49.458f, 68.361f, 51.038f, 66.772f, 52.665f)
                curveTo(35.75f, 84.506f, 22.569f, 135.421f, 27.64f, 178.349f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(39.254f, 181.232f)
                verticalLineTo(192.057f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(44.667f, 186.645f)
                horizontalLineTo(33.842f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(285.456f, 277.87f)
                verticalLineTo(284.544f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(288.793f, 281.207f)
                horizontalLineTo(282.119f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(320.723f, 51.961f)
                verticalLineTo(58.635f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(324.059f, 55.298f)
                horizontalLineTo(317.386f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(230.993f, 324.453f)
                verticalLineTo(331.127f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(234.33f, 327.79f)
                horizontalLineTo(227.656f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(316.487f, 309.572f)
                verticalLineTo(325.772f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(324.587f, 317.672f)
                horizontalLineTo(308.387f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(68.898f, 76.502f)
                verticalLineTo(92.703f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(76.998f, 84.603f)
                horizontalLineTo(60.798f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(42.894f, 130.067f)
                curveTo(42.894f, 131.682f, 41.585f, 132.99f, 39.971f, 132.99f)
                curveTo(38.356f, 132.99f, 37.047f, 131.682f, 37.047f, 130.067f)
                curveTo(37.047f, 128.453f, 38.356f, 127.144f, 39.971f, 127.144f)
                curveTo(41.585f, 127.144f, 42.894f, 128.453f, 42.894f, 130.067f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(331.516f, 299.908f)
                curveTo(331.516f, 301.026f, 330.609f, 301.932f, 329.492f, 301.932f)
                curveTo(328.374f, 301.932f, 327.468f, 301.026f, 327.468f, 299.908f)
                curveTo(327.468f, 298.791f, 328.374f, 297.885f, 329.492f, 297.885f)
                curveTo(330.609f, 297.885f, 331.516f, 298.79f, 331.516f, 299.908f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(257.844f, 313.813f)
                curveTo(257.844f, 314.931f, 256.938f, 315.837f, 255.821f, 315.837f)
                curveTo(254.703f, 315.837f, 253.797f, 314.931f, 253.797f, 313.813f)
                curveTo(253.797f, 312.696f, 254.703f, 311.789f, 255.821f, 311.789f)
                curveTo(256.938f, 311.789f, 257.844f, 312.695f, 257.844f, 313.813f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(50.725f, 144.579f)
                curveTo(52.181f, 141.311f, 50.711f, 137.482f, 47.443f, 136.026f)
                curveTo(44.175f, 134.571f, 40.346f, 136.041f, 38.891f, 139.309f)
                curveTo(37.436f, 142.577f, 38.905f, 146.406f, 42.173f, 147.861f)
                curveTo(45.441f, 149.316f, 49.27f, 147.847f, 50.725f, 144.579f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(340.888f, 199.069f)
                curveTo(344.465f, 199.069f, 347.365f, 196.168f, 347.365f, 192.591f)
                curveTo(347.365f, 189.014f, 344.465f, 186.114f, 340.888f, 186.114f)
                curveTo(337.31f, 186.114f, 334.41f, 189.014f, 334.41f, 192.591f)
                curveTo(334.41f, 196.168f, 337.31f, 199.069f, 340.888f, 199.069f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(140.257f, 61.811f)
                curveTo(141.076f, 58.329f, 138.917f, 54.842f, 135.435f, 54.023f)
                curveTo(131.953f, 53.204f, 128.466f, 55.363f, 127.647f, 58.845f)
                curveTo(126.828f, 62.328f, 128.987f, 65.814f, 132.469f, 66.633f)
                curveTo(135.952f, 67.452f, 139.438f, 65.293f, 140.257f, 61.811f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(127.312f, 95.851f)
                curveTo(87.257f, 95.851f, 54.673f, 127.756f, 54.673f, 166.967f)
                verticalLineTo(206.856f)
                horizontalLineTo(75.376f)
                verticalLineTo(166.967f)
                curveTo(75.376f, 138.933f, 98.673f, 116.121f, 127.313f, 116.121f)
                curveTo(155.946f, 116.121f, 179.244f, 138.933f, 179.244f, 166.967f)
                verticalLineTo(206.856f)
                horizontalLineTo(199.947f)
                verticalLineTo(166.967f)
                curveTo(199.945f, 127.756f, 167.361f, 95.851f, 127.312f, 95.851f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(205.58f, 315.413f)
                verticalLineTo(206.856f)
                horizontalLineTo(104.116f)
                moveTo(97.238f, 206.856f)
                horizontalLineTo(46.982f)
                verticalLineTo(338.052f)
                horizontalLineTo(205.58f)
                verticalLineTo(323.273f)
                moveTo(133.706f, 276.216f)
                lineTo(136.606f, 306.013f)
                horizontalLineTo(114.407f)
                lineTo(117.307f, 276.216f)
                curveTo(111.905f, 273.362f, 108.239f, 267.767f, 108.239f, 261.341f)
                curveTo(108.239f, 252.001f, 115.969f, 244.437f, 125.503f, 244.437f)
                curveTo(135.042f, 244.437f, 142.772f, 252.0f, 142.772f, 261.341f)
                curveTo(142.773f, 267.767f, 139.101f, 273.362f, 133.706f, 276.216f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(133.706f, 276.216f)
                lineTo(136.606f, 306.013f)
                horizontalLineTo(114.407f)
                lineTo(117.307f, 276.216f)
                curveTo(111.905f, 273.362f, 108.239f, 267.767f, 108.239f, 261.341f)
                curveTo(108.239f, 252.001f, 115.969f, 244.437f, 125.503f, 244.437f)
                curveTo(135.042f, 244.437f, 142.772f, 252.0f, 142.772f, 261.341f)
                curveTo(142.773f, 267.767f, 139.101f, 273.362f, 133.706f, 276.216f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(133.706f, 276.216f)
                lineTo(136.606f, 306.013f)
                horizontalLineTo(114.407f)
                lineTo(117.307f, 276.216f)
                curveTo(111.905f, 273.362f, 108.239f, 267.767f, 108.239f, 261.341f)
                curveTo(108.239f, 252.001f, 115.969f, 244.437f, 125.503f, 244.437f)
                curveTo(135.042f, 244.437f, 142.772f, 252.0f, 142.772f, 261.341f)
                curveTo(142.773f, 267.767f, 139.101f, 273.362f, 133.706f, 276.216f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSecondaryContainer), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(300.782f, 25.989f)
                curveTo(297.612f, 25.391f, 293.465f, 29.471f, 292.09f, 32.0f)
                curveTo(291.971f, 32.219f, 290.504f, 35.852f, 290.562f, 35.856f)
                curveTo(290.566f, 35.856f, 296.213f, 36.28f, 298.578f, 35.392f)
                curveTo(300.591f, 34.634f, 302.945f, 33.11f, 303.591f, 30.926f)
                curveTo(304.224f, 28.788f, 303.065f, 26.42f, 300.782f, 25.989f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSecondaryContainer), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(249.265f, 79.282f)
                curveTo(249.468f, 81.105f, 249.711f, 82.909f, 250.035f, 84.675f)
                curveTo(250.191f, 85.433f, 250.301f, 86.217f, 250.491f, 86.936f)
                curveTo(250.669f, 87.665f, 250.844f, 88.387f, 251.018f, 89.101f)
                curveTo(251.429f, 90.567f, 251.806f, 91.929f, 252.223f, 93.284f)
                curveTo(252.43f, 93.97f, 252.64f, 94.612f, 252.849f, 95.275f)
                curveTo(252.334f, 94.224f, 251.49f, 92.573f, 251.396f, 92.431f)
                curveTo(249.81f, 90.027f, 245.33f, 86.315f, 242.223f, 87.181f)
                curveTo(239.984f, 87.805f, 239.031f, 90.264f, 239.844f, 92.339f)
                curveTo(240.673f, 94.46f, 243.15f, 95.778f, 245.22f, 96.362f)
                curveTo(247.503f, 97.004f, 252.514f, 96.256f, 253.126f, 96.162f)
                curveTo(253.48f, 97.266f, 253.835f, 98.347f, 254.188f, 99.388f)
                horizontalLineTo(256.191f)
                curveTo(255.791f, 98.303f, 255.386f, 97.18f, 254.983f, 96.015f)
                curveTo(255.544f, 95.777f, 259.877f, 93.909f, 261.619f, 92.21f)
                curveTo(263.457f, 93.366f, 265.547f, 94.342f, 265.915f, 94.511f)
                curveTo(265.454f, 96.151f, 265.019f, 97.751f, 264.642f, 99.257f)
                curveTo(264.631f, 99.301f, 264.62f, 99.343f, 264.609f, 99.388f)
                horizontalLineTo(266.574f)
                curveTo(266.869f, 98.113f, 267.194f, 96.782f, 267.543f, 95.413f)
                curveTo(268.164f, 95.529f, 273.139f, 96.423f, 275.437f, 95.85f)
                curveTo(277.525f, 95.329f, 280.039f, 94.085f, 280.931f, 91.991f)
                curveTo(281.805f, 89.94f, 280.927f, 87.454f, 278.708f, 86.763f)
                curveTo(275.628f, 85.805f, 271.039f, 89.38f, 269.382f, 91.735f)
                curveTo(269.275f, 91.886f, 268.221f, 93.81f, 267.704f, 94.803f)
                curveTo(267.936f, 93.909f, 268.164f, 93.021f, 268.421f, 92.094f)
                curveTo(268.779f, 90.758f, 269.202f, 89.358f, 269.605f, 88.0f)
                curveTo(269.839f, 87.302f, 270.076f, 86.599f, 270.315f, 85.887f)
                curveTo(270.541f, 85.164f, 270.845f, 84.504f, 271.11f, 83.799f)
                curveTo(271.52f, 82.775f, 271.995f, 81.741f, 272.467f, 80.707f)
                curveTo(273.804f, 81.154f, 277.754f, 82.4f, 279.828f, 82.208f)
                curveTo(281.971f, 82.009f, 284.644f, 81.161f, 285.844f, 79.225f)
                curveTo(287.018f, 77.33f, 286.527f, 74.74f, 284.438f, 73.721f)
                curveTo(281.539f, 72.307f, 276.462f, 75.147f, 274.466f, 77.223f)
                curveTo(274.401f, 77.291f, 274.025f, 77.773f, 273.584f, 78.35f)
                curveTo(274.075f, 77.355f, 274.562f, 76.359f, 275.093f, 75.368f)
                curveTo(276.6f, 72.553f, 278.222f, 69.733f, 279.835f, 66.9f)
                curveTo(280.172f, 66.31f, 280.498f, 65.715f, 280.832f, 65.123f)
                curveTo(281.957f, 65.505f, 286.222f, 66.894f, 288.396f, 66.692f)
                curveTo(290.538f, 66.493f, 293.212f, 65.645f, 294.411f, 63.709f)
                curveTo(295.585f, 61.814f, 295.094f, 59.224f, 293.005f, 58.206f)
                curveTo(290.107f, 56.791f, 285.029f, 59.631f, 283.033f, 61.708f)
                curveTo(282.962f, 61.782f, 282.514f, 62.36f, 282.019f, 63.009f)
                curveTo(282.873f, 61.458f, 283.706f, 59.901f, 284.483f, 58.33f)
                curveTo(285.848f, 55.584f, 287.042f, 52.805f, 287.987f, 50.022f)
                curveTo(288.381f, 50.162f, 293.35f, 51.889f, 295.767f, 51.664f)
                curveTo(297.908f, 51.466f, 300.581f, 50.618f, 301.782f, 48.682f)
                curveTo(302.957f, 46.787f, 302.464f, 44.197f, 300.376f, 43.178f)
                curveTo(297.477f, 41.765f, 292.4f, 44.604f, 290.404f, 46.681f)
                curveTo(290.263f, 46.829f, 288.657f, 48.929f, 288.109f, 49.698f)
                curveTo(288.12f, 49.666f, 288.136f, 49.632f, 288.147f, 49.599f)
                curveTo(289.112f, 46.668f, 289.67f, 43.751f, 290.066f, 40.96f)
                curveTo(290.465f, 38.175f, 290.604f, 35.519f, 290.668f, 33.072f)
                curveTo(290.739f, 30.623f, 290.652f, 28.38f, 290.56f, 26.395f)
                curveTo(290.501f, 25.321f, 290.428f, 24.338f, 290.349f, 23.424f)
                curveTo(290.36f, 23.432f, 290.403f, 23.465f, 290.403f, 23.465f)
                curveTo(290.45f, 23.5f, 291.728f, 19.796f, 291.782f, 19.553f)
                curveTo(292.404f, 16.741f, 291.952f, 10.941f, 289.168f, 9.312f)
                curveTo(287.163f, 8.14f, 284.735f, 9.166f, 283.808f, 11.194f)
                curveTo(282.863f, 13.265f, 283.639f, 15.961f, 284.661f, 17.853f)
                curveTo(285.708f, 19.793f, 289.297f, 22.617f, 290.195f, 23.307f)
                curveTo(290.257f, 24.259f, 290.313f, 25.277f, 290.351f, 26.405f)
                curveTo(290.4f, 28.387f, 290.437f, 30.625f, 290.314f, 33.061f)
                curveTo(290.283f, 33.71f, 290.222f, 34.407f, 290.177f, 35.083f)
                curveTo(290.018f, 33.927f, 289.777f, 32.344f, 289.736f, 32.192f)
                curveTo(288.992f, 29.41f, 285.919f, 24.47f, 282.697f, 24.308f)
                curveTo(280.377f, 24.191f, 278.695f, 26.221f, 278.808f, 28.448f)
                curveTo(278.923f, 30.722f, 280.853f, 32.756f, 282.634f, 33.965f)
                curveTo(284.491f, 35.227f, 289.09f, 36.085f, 290.098f, 36.262f)
                curveTo(289.973f, 37.759f, 289.811f, 39.293f, 289.547f, 40.884f)
                curveTo(289.1f, 43.621f, 288.479f, 46.515f, 287.469f, 49.372f)
                curveTo(287.413f, 49.531f, 287.335f, 49.689f, 287.277f, 49.847f)
                curveTo(287.297f, 49.263f, 287.177f, 46.045f, 287.144f, 45.821f)
                curveTo(286.71f, 42.974f, 284.198f, 37.727f, 281.014f, 37.212f)
                curveTo(278.719f, 36.84f, 276.825f, 38.673f, 276.693f, 40.899f)
                curveTo(276.558f, 43.172f, 278.254f, 45.406f, 279.89f, 46.803f)
                curveTo(281.775f, 48.413f, 287.035f, 49.901f, 287.234f, 49.957f)
                curveTo(286.254f, 52.62f, 285.045f, 55.279f, 283.665f, 57.912f)
                curveTo(282.667f, 59.822f, 281.578f, 61.714f, 280.468f, 63.602f)
                curveTo(280.477f, 62.841f, 280.364f, 59.851f, 280.331f, 59.636f)
                curveTo(279.897f, 56.788f, 277.385f, 51.542f, 274.201f, 51.026f)
                curveTo(271.907f, 50.655f, 270.012f, 52.488f, 269.88f, 54.713f)
                curveTo(269.745f, 56.986f, 271.44f, 59.22f, 273.076f, 60.617f)
                curveTo(274.917f, 62.189f, 279.951f, 63.637f, 280.376f, 63.758f)
                curveTo(279.872f, 64.616f, 279.371f, 65.474f, 278.859f, 66.329f)
                curveTo(277.185f, 69.122f, 275.497f, 71.915f, 273.916f, 74.723f)
                curveTo(273.367f, 75.695f, 272.863f, 76.674f, 272.353f, 77.652f)
                curveTo(272.33f, 76.509f, 272.248f, 74.431f, 272.221f, 74.254f)
                curveTo(271.787f, 71.406f, 269.275f, 66.159f, 266.091f, 65.644f)
                curveTo(263.797f, 65.273f, 261.903f, 67.106f, 261.77f, 69.332f)
                curveTo(261.635f, 71.604f, 263.331f, 73.839f, 264.967f, 75.235f)
                curveTo(266.61f, 76.64f, 270.816f, 77.949f, 272.007f, 78.301f)
                curveTo(271.171f, 79.934f, 270.377f, 81.574f, 269.674f, 83.224f)
                curveTo(269.385f, 83.942f, 269.045f, 84.657f, 268.808f, 85.362f)
                curveTo(268.553f, 86.067f, 268.3f, 86.766f, 268.052f, 87.458f)
                curveTo(267.587f, 88.907f, 267.149f, 90.253f, 266.751f, 91.612f)
                curveTo(266.545f, 92.298f, 266.367f, 92.949f, 266.174f, 93.618f)
                curveTo(266.324f, 92.458f, 266.532f, 90.614f, 266.532f, 90.444f)
                curveTo(266.535f, 87.565f, 264.846f, 81.997f, 261.777f, 81.006f)
                curveTo(259.565f, 80.291f, 257.415f, 81.816f, 256.947f, 83.996f)
                curveTo(256.887f, 84.273f, 256.857f, 84.554f, 256.85f, 84.836f)
                curveTo(255.385f, 86.949f, 254.544f, 90.02f, 254.488f, 91.933f)
                curveTo(254.483f, 92.117f, 254.665f, 94.302f, 254.781f, 95.416f)
                curveTo(254.482f, 94.542f, 254.181f, 93.676f, 253.884f, 92.761f)
                curveTo(253.447f, 91.45f, 253.028f, 90.048f, 252.614f, 88.693f)
                curveTo(252.425f, 87.983f, 252.234f, 87.265f, 252.041f, 86.54f)
                curveTo(251.832f, 85.812f, 251.721f, 85.095f, 251.552f, 84.36f)
                curveTo(251.33f, 83.28f, 251.154f, 82.155f, 250.978f, 81.032f)
                curveTo(252.34f, 80.668f, 256.322f, 79.527f, 257.945f, 78.223f)
                curveTo(259.622f, 76.875f, 261.385f, 74.692f, 261.318f, 72.417f)
                curveTo(261.252f, 70.188f, 259.413f, 68.299f, 257.108f, 68.601f)
                curveTo(253.911f, 69.021f, 251.243f, 74.19f, 250.724f, 77.024f)
                curveTo(250.707f, 77.115f, 250.66f, 77.726f, 250.611f, 78.449f)
                curveTo(250.471f, 77.349f, 250.327f, 76.25f, 250.223f, 75.13f)
                curveTo(249.926f, 71.951f, 249.724f, 68.704f, 249.507f, 65.451f)
                curveTo(249.462f, 64.773f, 249.407f, 64.096f, 249.359f, 63.419f)
                curveTo(250.508f, 63.118f, 254.831f, 61.921f, 256.533f, 60.554f)
                curveTo(258.21f, 59.207f, 259.972f, 57.025f, 259.905f, 54.748f)
                curveTo(259.839f, 52.52f, 258.0f, 50.63f, 255.696f, 50.933f)
                curveTo(252.497f, 51.353f, 249.83f, 56.521f, 249.311f, 59.355f)
                curveTo(249.292f, 59.456f, 249.237f, 60.185f, 249.183f, 61.001f)
                curveTo(249.039f, 59.235f, 248.875f, 57.477f, 248.657f, 55.737f)
                curveTo(248.28f, 52.695f, 247.744f, 49.718f, 246.996f, 46.876f)
                curveTo(247.402f, 46.773f, 252.499f, 45.473f, 254.391f, 43.953f)
                curveTo(256.068f, 42.606f, 257.83f, 40.424f, 257.763f, 38.148f)
                curveTo(257.698f, 35.919f, 255.858f, 34.031f, 253.554f, 34.332f)
                curveTo(250.355f, 34.752f, 247.688f, 39.922f, 247.169f, 42.755f)
                curveTo(247.132f, 42.955f, 246.952f, 45.593f, 246.92f, 46.538f)
                curveTo(246.91f, 46.504f, 246.906f, 46.467f, 246.896f, 46.434f)
                curveTo(246.084f, 43.457f, 244.941f, 40.716f, 243.731f, 38.169f)
                curveTo(242.527f, 35.627f, 241.177f, 33.334f, 239.88f, 31.258f)
                curveTo(238.589f, 29.177f, 237.279f, 27.354f, 236.107f, 25.749f)
                curveTo(235.465f, 24.885f, 234.862f, 24.106f, 234.292f, 23.386f)
                curveTo(234.305f, 23.387f, 234.36f, 23.391f, 234.36f, 23.391f)
                curveTo(234.419f, 23.395f, 233.441f, 19.6f, 233.353f, 19.368f)
                curveTo(232.32f, 16.679f, 228.744f, 12.09f, 225.523f, 12.268f)
                curveTo(223.203f, 12.395f, 221.744f, 14.591f, 222.09f, 16.794f)
                curveTo(222.444f, 19.043f, 224.58f, 20.863f, 226.476f, 21.878f)
                curveTo(228.419f, 22.917f, 232.971f, 23.295f, 234.1f, 23.373f)
                curveTo(234.677f, 24.133f, 235.285f, 24.951f, 235.938f, 25.87f)
                curveTo(237.073f, 27.497f, 238.339f, 29.343f, 239.58f, 31.443f)
                curveTo(239.912f, 32.001f, 240.246f, 32.616f, 240.581f, 33.205f)
                curveTo(239.811f, 32.329f, 238.736f, 31.14f, 238.619f, 31.037f)
                curveTo(236.463f, 29.127f, 231.176f, 26.702f, 228.399f, 28.344f)
                curveTo(226.399f, 29.527f, 226.116f, 32.148f, 227.438f, 33.943f)
                curveTo(228.789f, 35.776f, 231.522f, 36.408f, 233.673f, 36.435f)
                curveTo(235.917f, 36.462f, 240.226f, 34.641f, 241.165f, 34.232f)
                curveTo(241.887f, 35.55f, 242.598f, 36.919f, 243.256f, 38.391f)
                curveTo(244.393f, 40.922f, 245.471f, 43.678f, 246.205f, 46.618f)
                curveTo(246.246f, 46.781f, 246.268f, 46.955f, 246.308f, 47.119f)
                curveTo(246.001f, 46.62f, 244.126f, 44.004f, 243.975f, 43.836f)
                curveTo(242.042f, 41.7f, 237.053f, 38.71f, 234.112f, 40.036f)
                curveTo(231.993f, 40.993f, 231.424f, 43.566f, 232.542f, 45.495f)
                curveTo(233.683f, 47.466f, 236.33f, 48.394f, 238.465f, 48.656f)
                curveTo(240.925f, 48.958f, 246.135f, 47.298f, 246.33f, 47.235f)
                curveTo(246.981f, 49.997f, 247.441f, 52.882f, 247.741f, 55.839f)
                curveTo(247.963f, 57.982f, 248.099f, 60.16f, 248.215f, 62.349f)
                curveTo(247.802f, 61.709f, 246.058f, 59.277f, 245.912f, 59.116f)
                curveTo(243.98f, 56.98f, 238.99f, 53.99f, 236.05f, 55.317f)
                curveTo(233.931f, 56.273f, 233.362f, 58.847f, 234.48f, 60.776f)
                curveTo(235.62f, 62.747f, 238.268f, 63.675f, 240.403f, 63.937f)
                curveTo(242.806f, 64.232f, 247.803f, 62.664f, 248.224f, 62.53f)
                curveTo(248.276f, 63.523f, 248.333f, 64.516f, 248.377f, 65.511f)
                curveTo(248.524f, 68.764f, 248.655f, 72.025f, 248.884f, 75.239f)
                curveTo(248.963f, 76.353f, 249.083f, 77.447f, 249.197f, 78.544f)
                curveTo(248.548f, 77.604f, 247.334f, 75.915f, 247.213f, 75.783f)
                curveTo(245.28f, 73.647f, 240.291f, 70.657f, 237.35f, 71.984f)
                curveTo(235.232f, 72.94f, 234.663f, 75.514f, 235.781f, 77.443f)
                curveTo(236.922f, 79.414f, 239.568f, 80.341f, 241.704f, 80.604f)
                curveTo(243.847f, 80.873f, 248.078f, 79.644f, 249.265f, 79.282f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSecondaryContainer), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(241.325f, 33.643f)
                curveTo(241.329f, 33.641f, 246.273f, 30.878f, 247.754f, 28.833f)
                curveTo(249.016f, 27.091f, 250.139f, 24.52f, 249.473f, 22.343f)
                curveTo(248.822f, 20.211f, 246.549f, 18.874f, 244.407f, 19.775f)
                curveTo(241.433f, 21.024f, 240.225f, 26.715f, 240.472f, 29.585f)
                curveTo(240.493f, 29.832f, 241.274f, 33.672f, 241.325f, 33.643f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSecondaryContainer), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(104.292f, 261.563f)
                curveTo(100.988f, 260.939f, 96.667f, 265.191f, 95.233f, 267.828f)
                curveTo(95.109f, 268.056f, 93.58f, 271.842f, 93.641f, 271.847f)
                curveTo(93.645f, 271.847f, 99.531f, 272.288f, 101.995f, 271.362f)
                curveTo(104.093f, 270.573f, 106.547f, 268.984f, 107.22f, 266.708f)
                curveTo(107.878f, 264.481f, 106.671f, 262.012f, 104.292f, 261.563f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSecondaryContainer), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(50.605f, 317.1f)
                curveTo(50.818f, 319.0f, 51.07f, 320.88f, 51.408f, 322.72f)
                curveTo(51.569f, 323.51f, 51.685f, 324.327f, 51.883f, 325.076f)
                curveTo(52.069f, 325.836f, 52.251f, 326.588f, 52.433f, 327.333f)
                curveTo(52.861f, 328.86f, 53.253f, 330.28f, 53.69f, 331.691f)
                curveTo(53.905f, 332.406f, 54.124f, 333.076f, 54.342f, 333.767f)
                curveTo(53.805f, 332.672f, 52.926f, 330.95f, 52.828f, 330.802f)
                curveTo(51.175f, 328.297f, 46.507f, 324.429f, 43.27f, 325.332f)
                curveTo(40.936f, 325.983f, 39.943f, 328.544f, 40.79f, 330.707f)
                curveTo(41.654f, 332.917f, 44.235f, 334.291f, 46.393f, 334.899f)
                curveTo(48.772f, 335.568f, 53.993f, 334.789f, 54.632f, 334.69f)
                curveTo(55.001f, 335.842f, 55.37f, 336.967f, 55.739f, 338.053f)
                horizontalLineTo(57.826f)
                curveTo(57.408f, 336.922f, 56.988f, 335.752f, 56.567f, 334.538f)
                curveTo(57.151f, 334.291f, 61.667f, 332.344f, 63.483f, 330.573f)
                curveTo(65.397f, 331.778f, 67.576f, 332.795f, 67.961f, 332.971f)
                curveTo(67.479f, 334.68f, 67.027f, 336.347f, 66.633f, 337.916f)
                curveTo(66.621f, 337.963f, 66.611f, 338.007f, 66.599f, 338.053f)
                horizontalLineTo(68.647f)
                curveTo(68.953f, 336.724f, 69.292f, 335.337f, 69.657f, 333.911f)
                curveTo(70.304f, 334.032f, 75.489f, 334.963f, 77.883f, 334.366f)
                curveTo(80.058f, 333.824f, 82.678f, 332.527f, 83.608f, 330.344f)
                curveTo(84.519f, 328.207f, 83.604f, 325.617f, 81.292f, 324.897f)
                curveTo(78.082f, 323.898f, 73.3f, 327.625f, 71.573f, 330.079f)
                curveTo(71.462f, 330.236f, 70.363f, 332.241f, 69.825f, 333.275f)
                curveTo(70.066f, 332.344f, 70.304f, 331.418f, 70.571f, 330.452f)
                curveTo(70.945f, 329.061f, 71.386f, 327.601f, 71.805f, 326.185f)
                curveTo(72.05f, 325.459f, 72.296f, 324.726f, 72.546f, 323.984f)
                curveTo(72.781f, 323.231f, 73.098f, 322.543f, 73.374f, 321.808f)
                curveTo(73.801f, 320.742f, 74.296f, 319.663f, 74.787f, 318.586f)
                curveTo(76.182f, 319.053f, 80.296f, 320.35f, 82.458f, 320.15f)
                curveTo(84.69f, 319.943f, 87.477f, 319.059f, 88.727f, 317.042f)
                curveTo(89.951f, 315.067f, 89.439f, 312.368f, 87.262f, 311.306f)
                curveTo(84.241f, 309.832f, 78.95f, 312.792f, 76.871f, 314.956f)
                curveTo(76.803f, 315.027f, 76.411f, 315.529f, 75.952f, 316.131f)
                curveTo(76.464f, 315.094f, 76.971f, 314.056f, 77.524f, 313.022f)
                curveTo(79.094f, 310.089f, 80.784f, 307.151f, 82.466f, 304.198f)
                curveTo(82.816f, 303.583f, 83.157f, 302.963f, 83.505f, 302.347f)
                curveTo(84.677f, 302.745f, 89.122f, 304.192f, 91.387f, 303.982f)
                curveTo(93.619f, 303.775f, 96.406f, 302.891f, 97.656f, 300.874f)
                curveTo(98.88f, 298.899f, 98.368f, 296.2f, 96.191f, 295.138f)
                curveTo(93.17f, 293.664f, 87.88f, 296.623f, 85.799f, 298.788f)
                curveTo(85.725f, 298.865f, 85.258f, 299.467f, 84.742f, 300.145f)
                curveTo(85.632f, 298.528f, 86.5f, 296.905f, 87.31f, 295.268f)
                curveTo(88.732f, 292.407f, 89.977f, 289.511f, 90.961f, 286.611f)
                curveTo(91.373f, 286.756f, 96.551f, 288.556f, 99.069f, 288.322f)
                curveTo(101.301f, 288.115f, 104.086f, 287.232f, 105.337f, 285.214f)
                curveTo(106.561f, 283.24f, 106.048f, 280.541f, 103.872f, 279.478f)
                curveTo(100.85f, 278.005f, 95.56f, 280.964f, 93.48f, 283.128f)
                curveTo(93.332f, 283.282f, 91.659f, 285.47f, 91.087f, 286.273f)
                curveTo(91.099f, 286.239f, 91.116f, 286.204f, 91.127f, 286.169f)
                curveTo(92.132f, 283.115f, 92.714f, 280.075f, 93.127f, 277.166f)
                curveTo(93.542f, 274.264f, 93.686f, 271.495f, 93.754f, 268.945f)
                curveTo(93.827f, 266.394f, 93.738f, 264.056f, 93.641f, 261.987f)
                curveTo(93.58f, 260.867f, 93.504f, 259.844f, 93.422f, 258.89f)
                curveTo(93.433f, 258.899f, 93.479f, 258.934f, 93.479f, 258.934f)
                curveTo(93.528f, 258.97f, 94.86f, 255.11f, 94.916f, 254.857f)
                curveTo(95.564f, 251.926f, 95.094f, 245.882f, 92.192f, 244.184f)
                curveTo(90.103f, 242.962f, 87.571f, 244.031f, 86.607f, 246.146f)
                curveTo(85.621f, 248.304f, 86.43f, 251.113f, 87.495f, 253.085f)
                curveTo(88.587f, 255.106f, 92.326f, 258.05f, 93.262f, 258.769f)
                curveTo(93.327f, 259.76f, 93.386f, 260.822f, 93.424f, 261.996f)
                curveTo(93.476f, 264.062f, 93.514f, 266.394f, 93.386f, 268.932f)
                curveTo(93.354f, 269.608f, 93.291f, 270.335f, 93.243f, 271.04f)
                curveTo(93.078f, 269.835f, 92.826f, 268.185f, 92.785f, 268.027f)
                curveTo(92.009f, 265.127f, 88.807f, 259.98f, 85.449f, 259.811f)
                curveTo(83.031f, 259.689f, 81.278f, 261.804f, 81.395f, 264.125f)
                curveTo(81.515f, 266.494f, 83.527f, 268.614f, 85.383f, 269.874f)
                curveTo(87.317f, 271.189f, 92.11f, 272.083f, 93.16f, 272.268f)
                curveTo(93.031f, 273.828f, 92.861f, 275.427f, 92.587f, 277.084f)
                curveTo(92.12f, 279.938f, 91.474f, 282.953f, 90.421f, 285.931f)
                curveTo(90.363f, 286.095f, 90.282f, 286.26f, 90.221f, 286.425f)
                curveTo(90.242f, 285.816f, 90.118f, 282.463f, 90.082f, 282.23f)
                curveTo(89.63f, 279.264f, 87.012f, 273.795f, 83.694f, 273.258f)
                curveTo(81.303f, 272.871f, 79.328f, 274.782f, 79.191f, 277.1f)
                curveTo(79.05f, 279.469f, 80.817f, 281.797f, 82.522f, 283.253f)
                curveTo(84.487f, 284.93f, 89.968f, 286.482f, 90.175f, 286.54f)
                curveTo(89.154f, 289.315f, 87.894f, 292.086f, 86.456f, 294.83f)
                curveTo(85.416f, 296.819f, 84.282f, 298.792f, 83.125f, 300.76f)
                curveTo(83.134f, 299.966f, 83.016f, 296.85f, 82.982f, 296.626f)
                curveTo(82.53f, 293.658f, 79.911f, 288.191f, 76.593f, 287.655f)
                curveTo(74.203f, 287.267f, 72.229f, 289.178f, 72.09f, 291.496f)
                curveTo(71.949f, 293.866f, 73.717f, 296.194f, 75.421f, 297.649f)
                curveTo(77.339f, 299.287f, 82.585f, 300.797f, 83.028f, 300.923f)
                curveTo(82.502f, 301.817f, 81.981f, 302.711f, 81.447f, 303.603f)
                curveTo(79.703f, 306.513f, 77.944f, 309.424f, 76.296f, 312.349f)
                curveTo(75.724f, 313.362f, 75.199f, 314.383f, 74.667f, 315.401f)
                curveTo(74.643f, 314.211f, 74.558f, 312.046f, 74.53f, 311.86f)
                curveTo(74.078f, 308.892f, 71.459f, 303.425f, 68.141f, 302.888f)
                curveTo(65.75f, 302.501f, 63.776f, 304.411f, 63.638f, 306.731f)
                curveTo(63.498f, 309.098f, 65.265f, 311.427f, 66.97f, 312.883f)
                curveTo(68.683f, 314.346f, 73.065f, 315.71f, 74.306f, 316.077f)
                curveTo(73.435f, 317.779f, 72.608f, 319.487f, 71.875f, 321.207f)
                curveTo(71.574f, 321.956f, 71.22f, 322.7f, 70.972f, 323.435f)
                curveTo(70.707f, 324.171f, 70.444f, 324.899f, 70.184f, 325.619f)
                curveTo(69.7f, 327.13f, 69.244f, 328.532f, 68.828f, 329.948f)
                curveTo(68.613f, 330.663f, 68.428f, 331.342f, 68.227f, 332.039f)
                curveTo(68.384f, 330.829f, 68.6f, 328.909f, 68.6f, 328.732f)
                curveTo(68.603f, 325.73f, 66.844f, 319.929f, 63.646f, 318.896f)
                curveTo(61.341f, 318.151f, 59.1f, 319.74f, 58.612f, 322.012f)
                curveTo(58.55f, 322.3f, 58.518f, 322.593f, 58.511f, 322.887f)
                curveTo(56.985f, 325.09f, 56.108f, 328.289f, 56.05f, 330.283f)
                curveTo(56.045f, 330.474f, 56.234f, 332.752f, 56.355f, 333.913f)
                curveTo(56.043f, 333.002f, 55.73f, 332.1f, 55.42f, 331.146f)
                curveTo(54.965f, 329.78f, 54.528f, 328.319f, 54.096f, 326.907f)
                curveTo(53.899f, 326.166f, 53.701f, 325.418f, 53.499f, 324.663f)
                curveTo(53.281f, 323.904f, 53.165f, 323.156f, 52.989f, 322.391f)
                curveTo(52.758f, 321.265f, 52.575f, 320.093f, 52.391f, 318.923f)
                curveTo(53.811f, 318.543f, 57.96f, 317.355f, 59.652f, 315.996f)
                curveTo(61.4f, 314.592f, 63.236f, 312.317f, 63.166f, 309.947f)
                curveTo(63.097f, 307.623f, 61.181f, 305.656f, 58.78f, 305.97f)
                curveTo(55.447f, 306.408f, 52.668f, 311.795f, 52.126f, 314.748f)
                curveTo(52.109f, 314.843f, 52.059f, 315.479f, 52.007f, 316.233f)
                curveTo(51.862f, 315.086f, 51.712f, 313.941f, 51.604f, 312.774f)
                curveTo(51.296f, 309.461f, 51.085f, 306.078f, 50.858f, 302.688f)
                curveTo(50.811f, 301.981f, 50.753f, 301.277f, 50.703f, 300.57f)
                curveTo(51.9f, 300.256f, 56.406f, 299.009f, 58.179f, 297.585f)
                curveTo(59.927f, 296.181f, 61.763f, 293.907f, 61.693f, 291.535f)
                curveTo(61.625f, 289.212f, 59.708f, 287.244f, 57.307f, 287.559f)
                curveTo(53.974f, 287.997f, 51.194f, 293.383f, 50.653f, 296.336f)
                curveTo(50.633f, 296.441f, 50.576f, 297.2f, 50.52f, 298.051f)
                curveTo(50.371f, 296.211f, 50.199f, 294.378f, 49.973f, 292.565f)
                curveTo(49.58f, 289.395f, 49.021f, 286.293f, 48.242f, 283.331f)
                curveTo(48.665f, 283.225f, 53.977f, 281.869f, 55.948f, 280.286f)
                curveTo(57.696f, 278.882f, 59.532f, 276.607f, 59.462f, 274.236f)
                curveTo(59.394f, 271.912f, 57.477f, 269.945f, 55.076f, 270.259f)
                curveTo(51.743f, 270.697f, 48.963f, 276.084f, 48.423f, 279.037f)
                curveTo(48.384f, 279.246f, 48.197f, 281.995f, 48.163f, 282.979f)
                curveTo(48.153f, 282.944f, 48.148f, 282.906f, 48.139f, 282.87f)
                curveTo(47.292f, 279.769f, 46.101f, 276.912f, 44.84f, 274.258f)
                curveTo(43.585f, 271.608f, 42.178f, 269.219f, 40.827f, 267.056f)
                curveTo(39.481f, 264.887f, 38.117f, 262.987f, 36.895f, 261.315f)
                curveTo(36.227f, 260.414f, 35.598f, 259.603f, 35.004f, 258.853f)
                curveTo(35.018f, 258.854f, 35.075f, 258.857f, 35.075f, 258.857f)
                curveTo(35.136f, 258.861f, 34.117f, 254.907f, 34.024f, 254.664f)
                curveTo(32.949f, 251.862f, 29.222f, 247.081f, 25.865f, 247.266f)
                curveTo(23.448f, 247.399f, 21.927f, 249.687f, 22.288f, 251.982f)
                curveTo(22.657f, 254.326f, 24.882f, 256.223f, 26.858f, 257.28f)
                curveTo(28.884f, 258.364f, 33.626f, 258.757f, 34.803f, 258.839f)
                curveTo(35.405f, 259.63f, 36.039f, 260.484f, 36.719f, 261.442f)
                curveTo(37.901f, 263.137f, 39.22f, 265.06f, 40.514f, 267.248f)
                curveTo(40.86f, 267.83f, 41.208f, 268.47f, 41.557f, 269.085f)
                curveTo(40.754f, 268.172f, 39.634f, 266.934f, 39.512f, 266.825f)
                curveTo(37.266f, 264.834f, 31.755f, 262.308f, 28.861f, 264.019f)
                curveTo(26.777f, 265.251f, 26.482f, 267.983f, 27.86f, 269.854f)
                curveTo(29.267f, 271.764f, 32.115f, 272.422f, 34.357f, 272.45f)
                curveTo(36.696f, 272.478f, 41.187f, 270.581f, 42.165f, 270.155f)
                curveTo(42.917f, 271.528f, 43.657f, 272.955f, 44.342f, 274.489f)
                curveTo(45.527f, 277.126f, 46.652f, 279.998f, 47.415f, 283.061f)
                curveTo(47.458f, 283.232f, 47.481f, 283.414f, 47.522f, 283.584f)
                curveTo(47.203f, 283.064f, 45.249f, 280.337f, 45.092f, 280.163f)
                curveTo(43.078f, 277.937f, 37.878f, 274.821f, 34.814f, 276.203f)
                curveTo(32.607f, 277.2f, 32.014f, 279.882f, 33.178f, 281.892f)
                curveTo(34.368f, 283.945f, 37.126f, 284.913f, 39.352f, 285.187f)
                curveTo(41.916f, 285.502f, 47.344f, 283.772f, 47.548f, 283.706f)
                curveTo(48.227f, 286.584f, 48.706f, 289.59f, 49.019f, 292.671f)
                curveTo(49.249f, 294.904f, 49.391f, 297.175f, 49.512f, 299.456f)
                curveTo(49.082f, 298.788f, 47.264f, 296.254f, 47.112f, 296.087f)
                curveTo(45.099f, 293.861f, 39.898f, 290.745f, 36.834f, 292.128f)
                curveTo(34.627f, 293.124f, 34.034f, 295.806f, 35.199f, 297.817f)
                curveTo(36.388f, 299.87f, 39.146f, 300.838f, 41.371f, 301.111f)
                curveTo(43.875f, 301.419f, 49.082f, 299.785f, 49.521f, 299.645f)
                curveTo(49.575f, 300.68f, 49.634f, 301.714f, 49.68f, 302.753f)
                curveTo(49.833f, 306.143f, 49.97f, 309.541f, 50.209f, 312.89f)
                curveTo(50.292f, 314.051f, 50.415f, 315.191f, 50.534f, 316.334f)
                curveTo(49.857f, 315.354f, 48.592f, 313.594f, 48.467f, 313.456f)
                curveTo(46.453f, 311.231f, 41.253f, 308.114f, 38.189f, 309.497f)
                curveTo(35.982f, 310.493f, 35.389f, 313.176f, 36.553f, 315.186f)
                curveTo(37.742f, 317.239f, 40.5f, 318.206f, 42.726f, 318.48f)
                curveTo(44.959f, 318.758f, 49.369f, 317.477f, 50.605f, 317.1f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSecondaryContainer), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(42.331f, 269.54f)
                curveTo(42.335f, 269.538f, 47.487f, 266.659f, 49.03f, 264.527f)
                curveTo(50.345f, 262.711f, 51.516f, 260.032f, 50.821f, 257.764f)
                curveTo(50.142f, 255.542f, 47.774f, 254.149f, 45.542f, 255.087f)
                curveTo(42.443f, 256.39f, 41.184f, 262.32f, 41.441f, 265.31f)
                curveTo(41.464f, 265.568f, 42.277f, 269.57f, 42.331f, 269.54f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onSurfaceVariant),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(333.55f, 286.959f)
                horizontalLineTo(171.027f)
                curveTo(167.161f, 286.959f, 164.026f, 283.825f, 164.026f, 279.958f)
                verticalLineTo(70.914f)
                horizontalLineTo(340.551f)
                verticalLineTo(279.959f)
                curveTo(340.551f, 283.825f, 337.417f, 286.959f, 333.55f, 286.959f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.background), stroke = SolidColor(colorScheme.onSurfaceVariant),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(329.856f, 283.728f)
                horizontalLineTo(167.333f)
                curveTo(163.466f, 283.728f, 160.332f, 280.593f, 160.332f, 276.727f)
                verticalLineTo(67.682f)
                horizontalLineTo(336.857f)
                verticalLineTo(276.726f)
                curveTo(336.857f, 280.593f, 333.722f, 283.728f, 329.856f, 283.728f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.background), stroke = SolidColor(colorScheme.onSurfaceVariant),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(329.986f, 67.682f)
                verticalLineTo(283.727f)
                horizontalLineTo(330.358f)
                curveTo(333.947f, 283.727f, 336.856f, 280.818f, 336.856f, 277.228f)
                verticalLineTo(67.682f)
                horizontalLineTo(329.986f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.background), stroke = SolidColor(colorScheme.onSurfaceVariant),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(336.857f, 125.203f)
                horizontalLineTo(329.986f)
                verticalLineTo(169.822f)
                horizontalLineTo(336.857f)
                verticalLineTo(125.203f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onSurfaceVariant),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(336.857f, 67.682f)
                horizontalLineTo(160.332f)
                verticalLineTo(82.963f)
                horizontalLineTo(336.857f)
                verticalLineTo(67.682f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.background), stroke = SolidColor(colorScheme.onSurfaceVariant),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(317.517f, 75.477f)
                curveTo(317.517f, 74.268f, 316.538f, 73.289f, 315.33f, 73.289f)
                curveTo(314.122f, 73.289f, 313.142f, 74.268f, 313.142f, 75.477f)
                curveTo(313.142f, 76.685f, 314.121f, 77.665f, 315.33f, 77.665f)
                curveTo(316.538f, 77.665f, 317.517f, 76.685f, 317.517f, 75.477f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.background), stroke = SolidColor(colorScheme.onSurfaceVariant),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(322.205f, 77.664f)
                curveTo(323.413f, 77.664f, 324.392f, 76.685f, 324.392f, 75.477f)
                curveTo(324.392f, 74.269f, 323.413f, 73.289f, 322.205f, 73.289f)
                curveTo(320.997f, 73.289f, 320.018f, 74.269f, 320.018f, 75.477f)
                curveTo(320.018f, 76.685f, 320.997f, 77.664f, 322.205f, 77.664f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.background), stroke = SolidColor(colorScheme.onSurfaceVariant),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(329.081f, 77.664f)
                curveTo(330.289f, 77.664f, 331.268f, 76.685f, 331.268f, 75.477f)
                curveTo(331.268f, 74.269f, 330.289f, 73.289f, 329.081f, 73.289f)
                curveTo(327.873f, 73.289f, 326.893f, 74.269f, 326.893f, 75.477f)
                curveTo(326.893f, 76.685f, 327.873f, 77.664f, 329.081f, 77.664f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.secondary), stroke = SolidColor(colorScheme.background),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(239.883f, 262.12f)
                curveTo(221.592f, 251.135f, 207.184f, 237.845f, 197.062f, 222.62f)
                curveTo(189.303f, 210.949f, 186.908f, 202.294f, 186.507f, 200.677f)
                lineTo(186.0f, 198.632f)
                verticalLineTo(120.841f)
                lineTo(248.754f, 94.0f)
                lineTo(311.509f, 120.841f)
                verticalLineTo(198.633f)
                lineTo(311.001f, 200.677f)
                curveTo(310.601f, 202.292f, 308.207f, 210.946f, 300.446f, 222.62f)
                curveTo(290.324f, 237.845f, 275.916f, 251.135f, 257.624f, 262.12f)
                lineTo(248.753f, 267.448f)
                lineTo(239.883f, 262.12f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.background), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(244.319f, 254.736f)
                curveTo(203.051f, 229.951f, 195.184f, 199.87f, 194.868f, 198.602f)
                lineTo(194.615f, 197.58f)
                verticalLineTo(126.527f)
                lineTo(248.754f, 103.37f)
                lineTo(302.894f, 126.527f)
                verticalLineTo(197.58f)
                lineTo(302.64f, 198.602f)
                curveTo(302.326f, 199.87f, 294.458f, 229.951f, 253.189f, 254.736f)
                lineTo(248.754f, 257.399f)
                lineTo(244.319f, 254.736f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.surfaceVariant), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(248.755f, 113.03f)
                lineTo(203.23f, 131.816f)
                verticalLineTo(196.527f)
                curveTo(203.23f, 196.527f, 210.077f, 224.121f, 248.755f, 247.35f)
                curveTo(287.433f, 224.121f, 294.28f, 196.527f, 294.28f, 196.527f)
                verticalLineTo(131.816f)
                lineTo(248.755f, 113.03f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.background), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(271.086f, 190.993f)
                curveTo(283.674f, 178.405f, 283.674f, 157.995f, 271.086f, 145.407f)
                curveTo(258.497f, 132.818f, 238.088f, 132.818f, 225.499f, 145.407f)
                curveTo(212.911f, 157.995f, 212.911f, 178.405f, 225.499f, 190.993f)
                curveTo(238.088f, 203.582f, 258.497f, 203.582f, 271.086f, 190.993f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSurfaceVariant), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(260.327f, 159.752f)
                curveTo(261.986f, 153.111f, 257.948f, 146.383f, 251.307f, 144.724f)
                curveTo(244.667f, 143.065f, 237.938f, 147.103f, 236.279f, 153.744f)
                curveTo(234.62f, 160.384f, 238.658f, 167.113f, 245.299f, 168.772f)
                curveTo(251.94f, 170.431f, 258.668f, 166.393f, 260.327f, 159.752f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.onSurfaceVariant), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(248.295f, 200.433f)
                curveTo(256.904f, 200.433f, 264.718f, 197.051f, 270.501f, 191.553f)
                curveTo(269.193f, 180.446f, 259.753f, 171.829f, 248.295f, 171.829f)
                curveTo(236.837f, 171.829f, 227.397f, 180.446f, 226.09f, 191.553f)
                curveTo(231.872f, 197.051f, 239.686f, 200.433f, 248.295f, 200.433f)
                close()
            }
            path(
                fill = SolidColor(colorScheme.outline), stroke = null, fillAlpha = 0.19f, strokeAlpha
                = 0.19f, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(257.625f, 262.12f)
                curveTo(275.917f, 251.135f, 290.325f, 237.845f, 300.447f, 222.62f)
                curveTo(308.208f, 210.946f, 310.6f, 202.292f, 311.002f, 200.677f)
                lineTo(311.51f, 198.633f)
                verticalLineTo(120.841f)
                lineTo(248.755f, 94.0f)
                verticalLineTo(267.449f)
                lineTo(257.625f, 262.12f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSecondaryContainer),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(15.324f, 338.053f)
                horizontalLineTo(348.738f)
            }
        }.build()
    }
}