package ua.polodarb.gmsflags.ui.images.welcomeimage

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
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
fun getGMSImagesWelcome(colorScheme: ColorScheme): ImageVector {
    return remember {
        Builder(
                name = "WelcomeImage", defaultWidth = 384.0.dp, defaultHeight =
                384.0.dp, viewportWidth = 384.0f, viewportHeight = 384.0f
            ).apply {
                path(
                    fill = SolidColor(colorScheme.surfaceVariant),
                    stroke = null,
                    strokeLineWidth = 0.0f,
                    strokeLineCap = Butt,
                    strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f,
                    pathFillType = NonZero
                ) {
                    moveTo(254.69f, 56.061f)
                    curveTo(283.258f, 65.612f, 312.402f, 85.573f, 316.836f, 114.221f)
                    curveTo(323.542f, 157.507f, 362.349f, 162.098f, 344.287f, 240.088f)
                    curveTo(341.473f, 252.248f, 337.464f, 263.508f, 332.469f, 273.873f)
                    curveTo(329.785f, 279.445f, 326.806f, 284.76f, 323.571f, 289.809f)
                    curveTo(294.266f, 335.658f, 243.868f, 360.111f, 195.356f, 362.818f)
                    curveTo(131.55f, 366.387f, 108.554f, 327.878f, 95.551f, 302.822f)
                    curveTo(82.549f, 277.764f, 19.696f, 248.285f, 36.774f, 171.601f)
                    curveTo(39.384f, 159.91f, 43.344f, 148.224f, 48.9f, 136.874f)
                    curveTo(61.569f, 110.965f, 83.906f, 84.484f, 112.302f, 68.86f)
                    curveTo(114.436f, 67.687f, 116.604f, 66.566f, 118.801f, 65.491f)
                    curveTo(161.776f, 44.506f, 215.743f, 43.039f, 254.69f, 56.061f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(111.116f, 54.979f)
                    lineTo(108.932f, 62.38f)
                    lineTo(113.791f, 68.374f)
                    lineTo(106.077f, 68.585f)
                    lineTo(101.877f, 75.057f)
                    lineTo(99.295f, 67.787f)
                    lineTo(91.84f, 65.792f)
                    lineTo(97.958f, 61.089f)
                    lineTo(97.55f, 53.383f)
                    lineTo(103.913f, 57.747f)
                    lineTo(111.116f, 54.979f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(271.257f, 113.496f)
                    lineTo(266.564f, 115.811f)
                    lineTo(265.812f, 120.991f)
                    lineTo(262.159f, 117.243f)
                    lineTo(257.0f, 118.127f)
                    lineTo(259.436f, 113.496f)
                    lineTo(257.0f, 108.863f)
                    lineTo(262.159f, 109.748f)
                    lineTo(265.812f, 106.0f)
                    lineTo(266.564f, 111.179f)
                    lineTo(271.257f, 113.496f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(309.514f, 190.45f)
                    lineTo(305.984f, 194.137f)
                    lineTo(307.02f, 199.136f)
                    lineTo(302.422f, 196.918f)
                    lineTo(297.989f, 199.449f)
                    lineTo(298.678f, 194.391f)
                    lineTo(294.901f, 190.956f)
                    lineTo(299.924f, 190.048f)
                    lineTo(302.024f, 185.395f)
                    lineTo(304.44f, 189.891f)
                    lineTo(309.514f, 190.45f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(111.442f, 182.388f)
                    lineTo(107.912f, 186.075f)
                    lineTo(108.949f, 191.074f)
                    lineTo(104.351f, 188.856f)
                    lineTo(99.918f, 191.386f)
                    lineTo(100.606f, 186.328f)
                    lineTo(96.829f, 182.894f)
                    lineTo(101.853f, 181.985f)
                    lineTo(103.952f, 177.332f)
                    lineTo(106.368f, 181.829f)
                    lineTo(111.442f, 182.388f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(281.179f, 321.338f)
                    lineTo(276.278f, 326.189f)
                    lineTo(277.495f, 332.976f)
                    lineTo(271.367f, 329.814f)
                    lineTo(265.288f, 333.069f)
                    lineTo(266.402f, 326.263f)
                    lineTo(261.428f, 321.488f)
                    lineTo(268.244f, 320.444f)
                    lineTo(271.249f, 314.238f)
                    lineTo(274.348f, 320.398f)
                    lineTo(281.179f, 321.338f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(67.77f, 234.931f)
                    lineTo(67.165f, 238.634f)
                    lineTo(69.873f, 241.231f)
                    lineTo(66.165f, 241.801f)
                    lineTo(64.532f, 245.178f)
                    lineTo(62.845f, 241.827f)
                    lineTo(59.127f, 241.318f)
                    lineTo(61.792f, 238.677f)
                    lineTo(61.129f, 234.985f)
                    lineTo(64.464f, 236.704f)
                    lineTo(67.77f, 234.931f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(347.021f, 259.33f)
                    lineTo(346.416f, 263.032f)
                    lineTo(349.124f, 265.63f)
                    lineTo(345.416f, 266.199f)
                    lineTo(343.783f, 269.576f)
                    lineTo(342.096f, 266.226f)
                    lineTo(338.378f, 265.717f)
                    lineTo(341.043f, 263.076f)
                    lineTo(340.38f, 259.384f)
                    lineTo(343.715f, 261.102f)
                    lineTo(347.021f, 259.33f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(96.283f, 336.93f)
                    lineTo(93.561f, 335.356f)
                    lineTo(90.72f, 336.706f)
                    lineTo(91.376f, 333.63f)
                    lineTo(89.214f, 331.346f)
                    lineTo(92.342f, 331.019f)
                    lineTo(93.846f, 328.257f)
                    lineTo(95.124f, 331.131f)
                    lineTo(98.216f, 331.708f)
                    lineTo(95.876f, 333.811f)
                    lineTo(96.283f, 336.93f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(55.949f, 133.791f)
                    lineTo(52.444f, 132.602f)
                    lineTo(49.478f, 134.816f)
                    lineTo(49.525f, 131.115f)
                    lineTo(46.504f, 128.979f)
                    lineTo(50.037f, 127.88f)
                    lineTo(51.136f, 124.346f)
                    lineTo(53.273f, 127.368f)
                    lineTo(56.973f, 127.32f)
                    lineTo(54.76f, 130.286f)
                    lineTo(55.949f, 133.791f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(322.551f, 139.825f)
                    lineTo(319.432f, 139.419f)
                    lineTo(317.329f, 141.757f)
                    lineTo(316.752f, 138.665f)
                    lineTo(313.878f, 137.388f)
                    lineTo(316.64f, 135.884f)
                    lineTo(316.966f, 132.756f)
                    lineTo(319.251f, 134.917f)
                    lineTo(322.326f, 134.262f)
                    lineTo(320.976f, 137.102f)
                    lineTo(322.551f, 139.825f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(209.629f, 40.531f)
                    lineTo(211.011f, 37.418f)
                    lineTo(209.229f, 34.514f)
                    lineTo(212.618f, 34.866f)
                    lineTo(214.828f, 32.274f)
                    lineTo(215.541f, 35.605f)
                    lineTo(218.689f, 36.907f)
                    lineTo(215.74f, 38.613f)
                    lineTo(215.475f, 42.01f)
                    lineTo(212.941f, 39.733f)
                    lineTo(209.629f, 40.531f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero
                ) {
                    moveTo(244.97f, 317.561f)
                    curveTo(275.837f, 286.694f, 275.837f, 236.649f, 244.97f, 205.782f)
                    curveTo(214.103f, 174.915f, 164.058f, 174.915f, 133.191f, 205.782f)
                    curveTo(102.324f, 236.649f, 102.324f, 286.694f, 133.191f, 317.561f)
                    curveTo(164.058f, 348.428f, 214.103f, 348.428f, 244.97f, 317.561f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(196.921f, 254.955f)
                    curveTo(206.195f, 254.955f, 213.714f, 247.562f, 213.714f, 238.442f)
                    curveTo(213.714f, 229.322f, 206.195f, 221.928f, 196.921f, 221.928f)
                    curveTo(187.646f, 221.928f, 180.127f, 229.322f, 180.127f, 238.442f)
                    curveTo(180.127f, 247.562f, 187.646f, 254.955f, 196.921f, 254.955f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(157.948f, 315.757f)
                    curveTo(161.52f, 310.762f, 158.302f, 302.339f, 150.759f, 296.943f)
                    curveTo(143.216f, 291.547f, 134.205f, 291.222f, 130.632f, 296.216f)
                    curveTo(127.059f, 301.211f, 130.278f, 309.634f, 137.821f, 315.03f)
                    curveTo(145.364f, 320.426f, 154.375f, 320.751f, 157.948f, 315.757f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(250.386f, 225.96f)
                    curveTo(251.951f, 231.942f, 249.583f, 237.743f, 245.096f, 238.917f)
                    curveTo(240.61f, 240.091f, 235.705f, 236.194f, 234.139f, 230.212f)
                    curveTo(232.573f, 224.23f, 234.941f, 218.429f, 239.428f, 217.255f)
                    curveTo(243.914f, 216.081f, 248.82f, 219.978f, 250.386f, 225.96f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(247.686f, 278.825f)
                    curveTo(246.401f, 282.784f, 242.953f, 285.213f, 239.983f, 284.25f)
                    curveTo(237.013f, 283.287f, 235.648f, 279.295f, 236.933f, 275.336f)
                    curveTo(238.217f, 271.377f, 241.666f, 268.949f, 244.636f, 269.912f)
                    curveTo(247.605f, 270.876f, 248.971f, 274.866f, 247.686f, 278.825f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(229.899f, 313.324f)
                    curveTo(227.314f, 316.586f, 223.235f, 317.658f, 220.788f, 315.719f)
                    curveTo(218.341f, 313.78f, 218.454f, 309.564f, 221.039f, 306.302f)
                    curveTo(223.624f, 303.04f, 227.704f, 301.967f, 230.15f, 303.907f)
                    curveTo(232.597f, 305.846f, 232.484f, 310.062f, 229.899f, 313.324f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(222.622f, 290.373f)
                    curveTo(220.037f, 293.635f, 215.958f, 294.708f, 213.511f, 292.768f)
                    curveTo(211.064f, 290.829f, 211.177f, 286.613f, 213.762f, 283.35f)
                    curveTo(216.347f, 280.088f, 220.427f, 279.016f, 222.874f, 280.955f)
                    curveTo(225.32f, 282.895f, 225.208f, 287.111f, 222.622f, 290.373f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onPrimary),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(131.66f, 273.626f)
                    curveTo(133.163f, 277.508f, 132.021f, 281.568f, 129.109f, 282.695f)
                    curveTo(126.198f, 283.822f, 122.62f, 281.589f, 121.117f, 277.707f)
                    curveTo(119.614f, 273.826f, 120.756f, 269.765f, 123.668f, 268.638f)
                    curveTo(126.579f, 267.511f, 130.157f, 269.745f, 131.66f, 273.626f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 4.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(244.653f, 38.438f)
                    lineTo(230.827f, 202.378f)
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(212.722f, 168.159f)
                    horizontalLineTo(162.943f)
                    curveTo(158.62f, 168.159f, 155.114f, 164.654f, 155.114f, 160.329f)
                    verticalLineTo(114.692f)
                    curveTo(155.114f, 110.368f, 158.619f, 106.863f, 162.943f, 106.863f)
                    horizontalLineTo(212.722f)
                    curveTo(217.047f, 106.863f, 220.552f, 110.368f, 220.552f, 114.692f)
                    verticalLineTo(160.329f)
                    curveTo(220.552f, 164.653f, 217.046f, 168.159f, 212.722f, 168.159f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.primaryContainer), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(198.732f, 119.676f)
                    curveTo(198.732f, 119.676f, 213.069f, 121.218f, 220.166f, 120.702f)
                    curveTo(227.263f, 120.186f, 230.618f, 118.38f, 230.618f, 118.38f)
                    lineTo(235.393f, 138.123f)
                    curveTo(235.393f, 138.123f, 233.07f, 140.574f, 225.328f, 142.38f)
                    curveTo(217.586f, 144.187f, 207.392f, 140.058f, 204.037f, 137.864f)
                    lineTo(198.732f, 119.676f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(235.392f, 138.122f)
                    lineTo(230.617f, 118.38f)
                    curveTo(230.617f, 118.38f, 227.499f, 120.05f, 220.938f, 120.635f)
                    curveTo(219.462f, 125.861f, 219.018f, 133.725f, 224.562f, 142.538f)
                    curveTo(224.819f, 142.491f, 225.075f, 142.44f, 225.327f, 142.38f)
                    curveTo(233.07f, 140.573f, 235.392f, 138.122f, 235.392f, 138.122f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(230.785f, 116.438f)
                    curveTo(230.785f, 116.438f, 227.961f, 115.526f, 226.749f, 116.762f)
                    curveTo(225.536f, 117.998f, 224.494f, 124.17f, 225.488f, 125.711f)
                    curveTo(226.482f, 127.252f, 228.684f, 127.149f, 229.291f, 126.531f)
                    curveTo(229.897f, 125.913f, 230.773f, 123.62f, 230.773f, 123.62f)
                    lineTo(233.08f, 117.314f)
                    lineTo(230.785f, 116.438f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(237.997f, 113.71f)
                    curveTo(235.721f, 113.497f, 232.787f, 113.481f, 231.696f, 114.876f)
                    curveTo(229.142f, 118.142f, 229.508f, 124.01f, 228.915f, 127.991f)
                    curveTo(228.62f, 129.97f, 228.348f, 132.052f, 228.6f, 134.051f)
                    curveTo(228.921f, 136.597f, 231.206f, 138.538f, 233.732f, 138.725f)
                    curveTo(236.853f, 138.955f, 240.401f, 137.524f, 242.781f, 135.568f)
                    curveTo(244.811f, 133.898f, 245.298f, 130.262f, 245.688f, 127.853f)
                    curveTo(246.241f, 124.432f, 246.505f, 120.873f, 245.987f, 117.433f)
                    curveTo(245.547f, 114.512f, 242.406f, 114.273f, 239.995f, 113.948f)
                    curveTo(239.472f, 113.878f, 238.773f, 113.783f, 237.997f, 113.71f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.primaryContainer), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(172.924f, 120.941f)
                    curveTo(172.924f, 120.941f, 154.591f, 118.673f, 146.464f, 126.044f)
                    curveTo(138.337f, 133.416f, 139.281f, 142.109f, 143.062f, 146.834f)
                    curveTo(146.842f, 151.559f, 152.134f, 152.693f, 152.134f, 152.693f)
                    lineTo(152.89f, 154.205f)
                    curveTo(152.89f, 154.205f, 150.055f, 154.205f, 151.945f, 156.662f)
                    curveTo(153.835f, 159.119f, 157.237f, 160.631f, 161.962f, 158.93f)
                    curveTo(166.687f, 157.229f, 165.364f, 149.48f, 165.364f, 149.48f)
                    curveTo(165.364f, 149.48f, 167.631f, 130.013f, 172.924f, 120.941f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.primaryContainer), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(148.031f, 124.821f)
                    curveTo(147.477f, 125.196f, 146.951f, 125.601f, 146.464f, 126.043f)
                    curveTo(138.337f, 133.415f, 139.281f, 142.108f, 143.062f, 146.833f)
                    curveTo(146.842f, 151.558f, 152.133f, 152.692f, 152.133f, 152.692f)
                    lineTo(152.889f, 154.204f)
                    curveTo(152.889f, 154.204f, 150.054f, 154.204f, 151.944f, 156.661f)
                    curveTo(153.834f, 159.118f, 157.236f, 160.63f, 161.961f, 158.929f)
                    curveTo(166.686f, 157.228f, 165.363f, 149.479f, 165.363f, 149.479f)
                    curveTo(165.363f, 149.479f, 165.67f, 146.859f, 166.317f, 143.098f)
                    curveTo(158.521f, 130.987f, 151.152f, 126.354f, 148.031f, 124.821f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(157.402f, 131.866f)
                    curveTo(147.842f, 132.725f, 143.228f, 138.878f, 141.112f, 143.37f)
                    curveTo(141.598f, 144.667f, 142.267f, 145.839f, 143.062f, 146.833f)
                    curveTo(146.842f, 151.558f, 152.134f, 152.692f, 152.134f, 152.692f)
                    lineTo(152.89f, 154.205f)
                    curveTo(152.89f, 154.205f, 150.055f, 154.204f, 151.945f, 156.661f)
                    curveTo(153.835f, 159.118f, 157.237f, 160.63f, 161.962f, 158.929f)
                    curveTo(166.687f, 157.228f, 165.364f, 149.479f, 165.364f, 149.479f)
                    curveTo(165.364f, 149.479f, 165.671f, 146.859f, 166.317f, 143.098f)
                    curveTo(163.194f, 138.248f, 160.141f, 134.599f, 157.402f, 131.866f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(152.89f, 154.205f)
                    curveTo(152.89f, 154.205f, 154.106f, 156.575f, 156.687f, 156.833f)
                }
                path(
                    fill = SolidColor(colorScheme.primaryContainer), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(216.694f, 190.589f)
                    curveTo(216.694f, 190.589f, 215.413f, 162.941f, 212.854f, 146.3f)
                    curveTo(210.294f, 129.66f, 203.125f, 119.676f, 203.125f, 119.676f)
                    lineTo(187.765f, 119.164f)
                    lineTo(172.405f, 119.676f)
                    curveTo(172.405f, 119.676f, 165.238f, 129.66f, 162.677f, 146.3f)
                    curveTo(160.117f, 162.941f, 158.837f, 190.589f, 158.837f, 190.589f)
                    curveTo(158.837f, 190.589f, 149.621f, 195.197f, 150.645f, 201.596f)
                    curveTo(151.668f, 207.996f, 160.884f, 208.508f, 168.565f, 206.46f)
                    curveTo(176.245f, 204.412f, 181.621f, 201.084f, 182.902f, 196.988f)
                    curveTo(184.182f, 192.893f, 184.438f, 187.004f, 184.438f, 187.004f)
                    lineTo(185.718f, 172.668f)
                    horizontalLineTo(189.814f)
                    lineTo(191.094f, 187.004f)
                    curveTo(191.094f, 187.004f, 191.35f, 192.893f, 192.63f, 196.988f)
                    curveTo(193.91f, 201.084f, 199.286f, 204.413f, 206.966f, 206.46f)
                    curveTo(214.646f, 208.508f, 223.862f, 207.996f, 224.886f, 201.596f)
                    curveTo(225.91f, 195.197f, 216.694f, 190.589f, 216.694f, 190.589f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.primaryContainer), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(187.765f, 125.021f)
                    curveTo(206.341f, 125.021f, 221.399f, 109.963f, 221.399f, 91.387f)
                    curveTo(221.399f, 72.812f, 206.341f, 57.754f, 187.765f, 57.754f)
                    curveTo(169.19f, 57.754f, 154.131f, 72.812f, 154.131f, 91.387f)
                    curveTo(154.131f, 109.963f, 169.19f, 125.021f, 187.765f, 125.021f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.primaryContainer), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(155.431f, 81.549f)
                    curveTo(156.871f, 81.549f, 158.039f, 80.382f, 158.039f, 78.941f)
                    curveTo(158.039f, 77.501f, 156.871f, 76.333f, 155.431f, 76.333f)
                    curveTo(153.99f, 76.333f, 152.823f, 77.501f, 152.823f, 78.941f)
                    curveTo(152.823f, 80.382f, 153.99f, 81.549f, 155.431f, 81.549f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(156.899f, 78.941f)
                    curveTo(156.899f, 78.13f, 156.242f, 77.473f, 155.431f, 77.473f)
                    curveTo(154.62f, 77.473f, 153.962f, 78.13f, 153.962f, 78.941f)
                    curveTo(153.962f, 79.752f, 154.62f, 80.41f, 155.431f, 80.41f)
                    curveTo(156.242f, 80.41f, 156.899f, 79.753f, 156.899f, 78.941f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(209.149f, 116.421f)
                    curveTo(200.512f, 122.392f, 175.018f, 122.392f, 166.381f, 116.421f)
                    curveTo(157.73f, 110.441f, 155.065f, 81.449f, 162.532f, 72.588f)
                    curveTo(170.89f, 62.668f, 204.641f, 62.668f, 212.999f, 72.588f)
                    curveTo(220.466f, 81.449f, 217.8f, 110.441f, 209.149f, 116.421f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(161.901f, 155.584f)
                    curveTo(161.901f, 155.584f, 173.209f, 155.683f, 185.717f, 172.668f)
                }
                path(
                    fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(213.695f, 155.104f)
                    curveTo(213.695f, 155.104f, 202.388f, 155.203f, 189.88f, 172.188f)
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(223.351f, 203.336f)
                    curveTo(223.351f, 203.336f, 222.044f, 207.912f, 220.083f, 208.893f)
                    curveTo(218.122f, 209.874f, 215.507f, 208.729f, 215.507f, 208.729f)
                    curveTo(215.507f, 208.729f, 212.729f, 209.874f, 210.768f, 209.71f)
                    curveTo(208.807f, 209.547f, 206.682f, 207.749f, 206.682f, 207.749f)
                    curveTo(206.682f, 207.749f, 203.904f, 208.567f, 201.943f, 207.749f)
                    curveTo(199.982f, 206.932f, 198.838f, 205.462f, 198.838f, 205.462f)
                    curveTo(198.838f, 205.462f, 196.713f, 205.625f, 194.915f, 203.827f)
                    curveTo(193.117f, 202.029f, 193.935f, 197.781f, 193.935f, 197.781f)
                    curveTo(193.935f, 197.781f, 214.69f, 204.154f, 223.351f, 203.336f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(224.886f, 201.597f)
                    curveTo(225.91f, 195.197f, 216.694f, 190.589f, 216.694f, 190.589f)
                    curveTo(216.694f, 190.589f, 216.508f, 186.582f, 216.137f, 180.703f)
                    curveTo(211.539f, 178.847f, 202.264f, 177.091f, 190.968f, 185.603f)
                    lineTo(191.093f, 187.006f)
                    curveTo(191.093f, 187.006f, 191.349f, 192.894f, 192.629f, 196.99f)
                    curveTo(193.909f, 201.086f, 199.285f, 204.414f, 206.965f, 206.462f)
                    curveTo(214.645f, 208.509f, 223.861f, 207.997f, 224.886f, 201.597f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(210.828f, 204.644f)
                    curveTo(218.153f, 205.146f, 222.517f, 203.307f, 224.797f, 201.813f)
                    curveTo(223.599f, 208.004f, 214.512f, 208.485f, 206.921f, 206.461f)
                    curveTo(199.241f, 204.413f, 193.865f, 201.085f, 192.584f, 196.989f)
                    curveTo(192.406f, 196.419f, 192.249f, 195.815f, 192.108f, 195.197f)
                    curveTo(195.571f, 198.521f, 202.525f, 204.075f, 210.828f, 204.644f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0x00000000)), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(162.677f, 146.3f)
                    curveTo(162.677f, 146.3f, 187.554f, 136.514f, 212.854f, 146.3f)
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(199.968f, 148.609f)
                    curveTo(195.348f, 152.867f, 179.456f, 152.867f, 174.835f, 148.609f)
                    curveTo(171.358f, 145.406f, 173.554f, 132.971f, 177.091f, 129.834f)
                    curveTo(180.948f, 126.413f, 193.855f, 126.413f, 197.712f, 129.834f)
                    curveTo(201.249f, 132.971f, 203.445f, 145.406f, 199.968f, 148.609f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(181.217f, 143.598f)
                    curveTo(181.217f, 145.189f, 179.927f, 146.479f, 178.336f, 146.479f)
                    curveTo(176.744f, 146.479f, 175.454f, 145.189f, 175.454f, 143.598f)
                    curveTo(175.454f, 142.006f, 176.744f, 140.716f, 178.336f, 140.716f)
                    curveTo(179.927f, 140.716f, 181.217f, 142.006f, 181.217f, 143.598f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(187.622f, 146.479f)
                    curveTo(189.213f, 146.479f, 190.503f, 145.189f, 190.503f, 143.598f)
                    curveTo(190.503f, 142.006f, 189.213f, 140.716f, 187.622f, 140.716f)
                    curveTo(186.03f, 140.716f, 184.74f, 142.006f, 184.74f, 143.598f)
                    curveTo(184.74f, 145.189f, 186.03f, 146.479f, 187.622f, 146.479f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(199.789f, 143.598f)
                    curveTo(199.789f, 145.189f, 198.499f, 146.479f, 196.907f, 146.479f)
                    curveTo(195.316f, 146.479f, 194.026f, 145.189f, 194.026f, 143.598f)
                    curveTo(194.026f, 142.006f, 195.316f, 140.716f, 196.907f, 140.716f)
                    curveTo(198.499f, 140.716f, 199.789f, 142.006f, 199.789f, 143.598f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(194.819f, 137.354f)
                    horizontalLineTo(180.305f)
                    curveTo(179.803f, 137.354f, 179.477f, 136.901f, 179.574f, 136.354f)
                    lineTo(180.159f, 133.054f)
                    curveTo(180.24f, 132.595f, 180.667f, 132.23f, 181.112f, 132.23f)
                    horizontalLineTo(194.011f)
                    curveTo(194.458f, 132.23f, 194.883f, 132.595f, 194.964f, 133.054f)
                    lineTo(195.55f, 136.354f)
                    curveTo(195.646f, 136.901f, 195.32f, 137.354f, 194.819f, 137.354f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(152.074f, 203.336f)
                    curveTo(152.074f, 203.336f, 153.381f, 207.912f, 155.343f, 208.893f)
                    curveTo(157.303f, 209.874f, 159.918f, 208.729f, 159.918f, 208.729f)
                    curveTo(159.918f, 208.729f, 162.696f, 209.874f, 164.658f, 209.71f)
                    curveTo(166.618f, 209.547f, 168.743f, 207.749f, 168.743f, 207.749f)
                    curveTo(168.743f, 207.749f, 171.521f, 208.567f, 173.483f, 207.749f)
                    curveTo(175.443f, 206.932f, 176.588f, 205.462f, 176.588f, 205.462f)
                    curveTo(176.588f, 205.462f, 178.712f, 205.625f, 180.51f, 203.827f)
                    curveTo(182.308f, 202.029f, 181.491f, 197.781f, 181.491f, 197.781f)
                    curveTo(181.491f, 197.781f, 160.735f, 204.154f, 152.074f, 203.336f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(159.364f, 181.167f)
                    curveTo(159.012f, 186.791f, 158.836f, 190.588f, 158.836f, 190.588f)
                    curveTo(158.836f, 190.588f, 149.62f, 195.196f, 150.644f, 201.596f)
                    curveTo(151.668f, 207.996f, 160.884f, 208.508f, 168.564f, 206.46f)
                    curveTo(176.244f, 204.412f, 181.62f, 201.084f, 182.901f, 196.988f)
                    curveTo(184.181f, 192.892f, 184.437f, 187.004f, 184.437f, 187.004f)
                    lineTo(184.626f, 184.891f)
                    curveTo(172.828f, 176.52f, 163.363f, 179.267f, 159.364f, 181.167f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(164.658f, 204.644f)
                    curveTo(157.333f, 205.146f, 152.969f, 203.307f, 150.689f, 201.813f)
                    curveTo(151.887f, 208.004f, 160.974f, 208.485f, 168.565f, 206.461f)
                    curveTo(176.245f, 204.413f, 181.621f, 201.085f, 182.901f, 196.989f)
                    curveTo(183.08f, 196.419f, 183.237f, 195.815f, 183.378f, 195.197f)
                    curveTo(179.915f, 198.521f, 172.961f, 204.075f, 164.658f, 204.644f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = SolidColor(colorScheme.onSurfaceVariant),
                    strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(207.382f, 114.487f)
                    curveTo(199.46f, 119.965f, 176.072f, 119.965f, 168.149f, 114.487f)
                    curveTo(160.213f, 109.002f, 157.768f, 82.406f, 164.618f, 74.276f)
                    curveTo(172.285f, 65.176f, 203.246f, 65.176f, 210.914f, 74.276f)
                    curveTo(217.763f, 82.406f, 215.317f, 109.001f, 207.382f, 114.487f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary),
                    stroke = null,
                    fillAlpha = 0.5f,
                    strokeAlpha
                    = 0.5f,
                    strokeLineWidth = 0.0f,
                    strokeLineCap = Butt,
                    strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f,
                    pathFillType = NonZero
                ) {
                    moveTo(209.772f, 75.349f)
                    curveTo(207.686f, 72.873f, 203.773f, 71.112f, 199.087f, 70.063f)
                    curveTo(192.883f, 72.923f, 184.907f, 77.769f, 178.244f, 85.644f)
                    curveTo(178.244f, 85.644f, 195.056f, 78.921f, 210.533f, 76.398f)
                    curveTo(210.294f, 76.021f, 210.041f, 75.669f, 209.772f, 75.349f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onPrimary),
                    stroke = null,
                    fillAlpha = 0.5f,
                    strokeAlpha
                    = 0.5f,
                    strokeLineWidth = 0.0f,
                    strokeLineCap = Butt,
                    strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f,
                    pathFillType = NonZero
                ) {
                    moveTo(213.088f, 84.142f)
                    curveTo(202.977f, 87.871f, 182.138f, 96.727f, 167.119f, 110.978f)
                    curveTo(167.807f, 111.966f, 168.562f, 112.767f, 169.378f, 113.331f)
                    curveTo(176.861f, 118.504f, 198.952f, 118.504f, 206.436f, 113.331f)
                    curveTo(211.997f, 109.487f, 214.703f, 94.671f, 213.088f, 84.142f)
                    close()
                }
                path(
                    fill = SolidColor(colorScheme.onSurfaceVariant), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd
                ) {
                    moveTo(279.332f, 39.333f)
                    lineTo(244.546f, 41.006f)
                    lineTo(240.781f, 86.434f)
                    lineTo(261.217f, 85.451f)
                    lineTo(260.385f, 95.488f)
                    curveTo(260.115f, 98.755f, 262.805f, 101.515f, 266.106f, 101.356f)
                    lineTo(296.108f, 99.913f)
                    curveTo(298.846f, 99.781f, 301.06f, 97.658f, 301.284f, 94.947f)
                    lineTo(304.146f, 60.423f)
                    curveTo(304.416f, 57.156f, 301.726f, 54.396f, 298.425f, 54.555f)
                    lineTo(284.22f, 55.238f)
                    lineTo(285.052f, 45.201f)
                    curveTo(285.323f, 41.934f, 282.632f, 39.174f, 279.332f, 39.333f)
                    close()
                    moveTo(284.22f, 55.238f)
                    lineTo(268.423f, 55.998f)
                    curveTo(265.685f, 56.13f, 263.471f, 58.254f, 263.247f, 60.964f)
                    lineTo(261.217f, 85.451f)
                    lineTo(277.014f, 84.691f)
                    curveTo(279.752f, 84.559f, 281.966f, 82.435f, 282.191f, 79.725f)
                    lineTo(284.22f, 55.238f)
                    close()
                }
            }.build()
        }
}
