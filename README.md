DroidAlone
=========

DroidAlone is a full android application available on the playstore.
It allows users to remotely enable missed calls / sms forwarding to a pre choosen email address. It's intended to be used when you forget your phone at home and you want to be notified of who's calling you. 

The application has been switched to maven, so you need to install several libraries including gridlayout support library, support library and android using the maven sdk deployer. 

All the events are catched by a broadcast receiver and handled by intent services using the command pattern. The resource consumption is limited to event handling since nothing is running in background. 

ActionbarSherlock is included for back compatible actionbar in the ui.

How it works:
-------------
The sms intercepted by the app are like
 
 * #password-e-s:on-m:fedepaol@gmail.com-sms:3286991883-r:I left my phone at home. Call me at office-g:fedepaol
 * e : sends an echo message to show the state
 * s + on / off: status. May be on or off
 * m + mail address : mail to send the notifications  to. Null disables mail notifications
 * sms + number : number to send the notifications to. Null disables sms notifications
 * r + reply: reply message to caller / sms sender. Null disables replies
 * g + name: tries to retrieve the number(s) associated to the name and returns them
 * 
 * Only password #password shows help

More details in the [official site]

Please read COPYING for copying license. 

[official site]: http://www.fedepaolapps.com/droidalone-manual/
 
