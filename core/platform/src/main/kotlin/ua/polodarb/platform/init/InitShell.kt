package ua.polodarb.platform.init

import com.topjohnwu.superuser.Shell

object InitShell {

    private const val SHELL_TIMEOUT = 10L

    private val shellConfig = Shell.Builder.create()
        .setFlags(Shell.FLAG_REDIRECT_STDERR or Shell.FLAG_MOUNT_MASTER)
        .setTimeout(SHELL_TIMEOUT)

    fun initShell() {
        try {
            Shell.setDefaultBuilder(shellConfig)
        } catch (_: IllegalStateException) {
            /* When application configuration changed (ex. orientation changed)
             * this function are called again, since onCreate in MainActivity
             * are called again, and since shell instance can be already created
             * it throws runtime exception, so just ignore it, since this call is ok.
             */
        }
    }

}