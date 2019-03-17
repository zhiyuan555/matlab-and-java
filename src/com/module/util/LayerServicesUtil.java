package com.module.util;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
/**
 * @author MXG
 *
 */
public class LayerServicesUtil {
	public static ArrayList<String> getLayerServices(String path){
		String[] layerServicesName = null;
		ArrayList<String> list=new ArrayList<String>();
		File file=new File(path);
		layerServicesName=file.list();
		String reg=".*_MapServer.*";
		for (String string : layerServicesName) {
			if (string.matches(reg)) {
				new StringUtils();
				list.add(StringUtils.substringBefore(string, "_"));
			}
		}
		return list;
		
	}
}
