package com.bairock.iot.hamaCenter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.config")
public class Config {

    private int padPort = 20000;
    private int devicePort = 20001;
    
    public int getPadPort() {
		return padPort;
	}
	public void setPadPort(int padPort) {
		this.padPort = padPort;
	}
	public int getDevicePort() {
        return devicePort;
    }
    public void setDevicePort(int devicePort) {
        this.devicePort = devicePort;
    }

}