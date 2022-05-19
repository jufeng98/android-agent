package org.javamaster.agent.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.os.SystemClock
import android.widget.Toast
import org.javamaster.agent.common.App
import org.javamaster.agent.receiver.ScheduledReceiver
import org.javamaster.agent.utils.RootCmd
import java.lang.Exception

/**
 * @author yudong
 * @date 2020/6/17
 */
class AgentHostService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAtTime = SystemClock.elapsedRealtime() + 3 * 1000
        val receiverIntent = Intent(this, ScheduledReceiver::class.java)
        val pi = PendingIntent.getBroadcast(this, 1, receiverIntent, PendingIntent.FLAG_ONE_SHOT)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi)

        try {
            val content = RootCmd.exusecmd(
                "cat " + Environment.getExternalStorageDirectory().path + "/Pictures/hosts.txt"
            )
            if (hostsContent != content) {
                RootCmd.modifyHosts(content)
                hostsContent = content
                Toast.makeText(App.context, "hosts文件已成功修改", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(App.context, "修改hosts出错了:" + e.message, Toast.LENGTH_SHORT).show()
        }

        return super.onStartCommand(receiverIntent, flags, startId)
    }

    companion object {

        lateinit var hostsContent: String

        @JvmStatic
        fun startService(context: Context) {
            hostsContent = RootCmd.exusecmd("cat /system/etc/hosts")
            val intent = Intent(context, AgentHostService::class.java)
            context.startService(intent)
            Toast.makeText(App.context, "修改hosts服务已启动", Toast.LENGTH_SHORT).show()
        }

    }

}