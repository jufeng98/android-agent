package org.javamaster.agent.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.javamaster.agent.service.AgentHostService

class ScheduledReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AgentHostService.startService(context)
    }

}
