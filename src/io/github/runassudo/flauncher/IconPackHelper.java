//    FLauncher
//    Copyright © 2015  RunasSudo (Yingtong Li)
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package io.github.runassudo.flauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

public class IconPackHelper {
    private static final String TAG = "FLauncher.IconPackHelpr";

    private final Context mContext;

    private final String mIconPack;

    private Resources packResources;
    private HashMap<String, String> componentDrawables = new HashMap<>();

    public IconPackHelper(Context context, String iconPack) {
        mContext = context;

        mIconPack = iconPack;
        loadIconPackResources();
    }

    //Reference: https://stackoverflow.com/questions/24937890/using-icon-packs-in-my-app
    void loadIconPackResources() {
        try {
            packResources = mContext.getPackageManager().getResourcesForApplication(mIconPack);

            // Load appfilter.xml
            int i = packResources.getIdentifier("appfilter", "xml", SettingsData.iconPackName);
            XmlPullParser appfilter = packResources.getXml(i);
            // TODO: Also try assets/

            // Parse appfilter.xml
            int eventType = appfilter.getEventType();
            do {
                if (eventType == XmlPullParser.START_TAG && appfilter.getName().equalsIgnoreCase("item")) {
                    String component = appfilter.getAttributeValue(null, "component");
                    String drawable = appfilter.getAttributeValue(null, "drawable");

                    // TODO: Validate input
                    component = component.substring("ComponentInfo{".length(), component.length() - "}".length()).toLowerCase();

                    if (component.contains("/")) {
                        ComponentName componentName = ComponentName.unflattenFromString(component);
                        componentDrawables.put(componentName.getPackageName(), drawable);
                        componentDrawables.put(componentName.getClassName(), drawable);
                    } else {
                        componentDrawables.put(component, drawable);
                    }
                }
            } while ((eventType = appfilter.next()) != XmlPullParser.END_DOCUMENT);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unknown package " + SettingsData.iconPackName);
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Unable to parse icon pack xml", e);
        }
    }

    public String getIconName(String component) {
        return componentDrawables.get(component);
    }

    public String getIconName(ComponentName component) {
        // Try the specific activity first, then the general package.
        String iconName = getIconName(component.flattenToString());
        if (iconName == null) {
            iconName = getIconName(component.getPackageName());
        }
        return iconName;
    }

    public int getIdFromName(String iconName) {
        return packResources.getIdentifier(iconName, "drawable", mIconPack);
    }
}
