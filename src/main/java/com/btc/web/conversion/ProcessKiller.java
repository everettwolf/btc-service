package com.btc.web.conversion;

/**
 * Created by Chris on 9/19/15.
 */
class ProcessKiller extends Thread {
     private Process process;

     public ProcessKiller(Process process) {
          this.process = process;
     }

     public void run() {
          this.process.destroy();
     }
}
