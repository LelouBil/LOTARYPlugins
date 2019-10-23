package fr.opperdev.lotaryapi.utils;

import java.util.Vector;

public strictfp class NativeMethods<n> {
	
	n n;
	
	byte[] usB;
	
	public NativeMethods(byte[] usignedBuffer){
		usB = usignedBuffer;
		// (Use it for uitppath)
	}
	
	public byte[] getUnsignedBuffer(){
		return usB;
	}
	
	{System.loadLibrary("LIBRARY");}
	public native <uint> int getUITPPath(String pop, int type, byte[] buffer, double[][][] threeDView);
	{System.loadLibrary("LIBRARY");}
	public native Vector<TriDIM<n>> getTDM(long unsignedWindowPop, int uitpPath, double[][] yz);
	
	class TriDIM<a> {
		transient a t;
		{System.loadLibrary("LIBRARY");}
		native int id();
		double[][][] threeDirections;
		public TriDIM(double[][][] tD){
			threeDirections = tD;
		}
		public a get() {
			used = true;
			return t;
		}
		private volatile boolean used;
		public synchronized void i() {
			threeDirections[id()][id()][id()] = id() - (Integer) t;
		}
		public void update() {
			synchronized(t) {
				used = t.toString().equals("true") ? true : false;
			}
		}
	}
		
}