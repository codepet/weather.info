package com.gc.weather.common;

import com.gc.weather.R;

public class ResourceUtil {

    public static int getImageResource(String type) {
        switch (type) {
            case "晴":
                return R.mipmap.sunny;
            case "多云":
                return R.mipmap.cloud;
            case "阴":
                return R.mipmap.overcast;
            case "阵雨":
                return R.mipmap.shower;
            case "雷阵雨":
                return R.mipmap.thundershower;
            case "雷阵雨伴有冰雹":
                return R.mipmap.thundershowerwithhail;
            case "雨夹雪":
                return R.mipmap.sleet;
            case "小雨":
                return R.mipmap.lightrain;
            case "中雨":
                return R.mipmap.moderaterain;
            case "大雨":
                return R.mipmap.heavyrain;
            case "暴雨":
                return R.mipmap.storm;
            case "大暴雨":
                return R.mipmap.heavystorm;
            case "特大暴雨":
                return R.mipmap.severestorm;
            case "阵雪":
                return R.mipmap.snowflurry;
            case "小雪":
                return R.mipmap.lightsnow;
            case "中雪":
                return R.mipmap.moderatesnow;
            case "大雪":
                return R.mipmap.heavysnow;
            case "暴雪":
                return R.mipmap.snowstorm;
            case "雾":
                return R.mipmap.foggy;
            case "冻雨":
                return R.mipmap.icerain;
            case "沙尘暴":
                return R.mipmap.duststorm;
            case "小到中雨":
                return R.mipmap.lighttomoderaterain;
            case "中到大雨":
                return R.mipmap.moderatetoheavyrain;
            case "大到暴雨":
                return R.mipmap.heavyraintostorm;
            case "暴雨到大暴雨":
                return R.mipmap.stormtoheavystorm;
            case "大暴雨到特大暴雨":
                return R.mipmap.heavytoseverestorm;
            case "小到中雪":
                return R.mipmap.lighttomoderatesnow;
            case "中到大雪":
                return R.mipmap.moderatetoheavysnow;
            case "大到暴雪":
                return R.mipmap.heavysnowtosnowstorm;
            case "浮尘":
                return R.mipmap.dust;
            case "扬沙":
                return R.mipmap.sand;
            case "强沙尘暴":
                return R.mipmap.sandstorm;
            case "霾":
                return R.mipmap.haze;
        }
        return R.mipmap.undefined;
    }

}
