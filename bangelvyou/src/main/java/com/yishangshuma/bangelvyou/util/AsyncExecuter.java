package com.yishangshuma.bangelvyou.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncExecuter {
	private static AsyncExecuter sAsyncExecuter;
	private ExecutorService mExecutor;

	private AsyncExecuter() {
		mExecutor = Executors.newFixedThreadPool(5);
	}

	public static AsyncExecuter getInstance() {
		if (sAsyncExecuter == null) {
			sAsyncExecuter = new AsyncExecuter();
		}

		return sAsyncExecuter;
	}

	public void execute(Runnable command) {
		if(mExecutor == null)
			mExecutor = Executors.newFixedThreadPool(2);
		mExecutor.execute(command);
	}
	
	public void shutdown()
	{
		mExecutor.shutdownNow();
		mExecutor = null;
		sAsyncExecuter = null;
		
	}
}
