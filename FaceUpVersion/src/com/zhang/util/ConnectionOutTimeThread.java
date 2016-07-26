package com.zhang.util;

public class ConnectionOutTimeThread implements Runnable{

	private long startTime = 0L;
	public boolean running = false;//此线程在不在运行
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(!this.running){
				return;
			}
			if(System.currentTimeMillis() - this.startTime >= 20 * 1000L){
				stop();
			}
			try {
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		
	}

	
}
