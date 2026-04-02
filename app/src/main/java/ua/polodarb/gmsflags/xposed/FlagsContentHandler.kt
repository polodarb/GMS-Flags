package ua.polodarb.gmsflags.xposed

interface FlagsContentHandler {
    fun handle(packageId: Int, content: ByteArray): ByteArray
}