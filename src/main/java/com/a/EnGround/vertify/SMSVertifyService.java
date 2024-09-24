package com.a.EnGround.vertify;

import java.util.HashMap;
import java.util.Map;
//
public interface SMSVertifyService {
	public <K, V> Object sendOne(HashMap<K, V> params);
	public String createVertifyCode();
	public <T> T setParam(String receiver);
	public <K, V> void apiAuthentication(HashMap<K, V> params);
	}
