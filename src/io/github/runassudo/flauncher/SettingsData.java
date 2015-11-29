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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsData {
    protected SettingsData() {}

    public static String desktopGridMode;
    public static int desktopGridCustomWidth, desktopGridCustomHeight;
    public static int desktopDefaultScreen;
    public static boolean desktopShowSearchbar;

    public static void loadSettings(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        desktopGridMode = sharedPref.getString("desktop_grid_mode", "default");
        desktopGridCustomWidth = Integer.parseInt(sharedPref.getString("desktop_grid_custom_width", "5"));
        desktopGridCustomHeight = Integer.parseInt(sharedPref.getString("desktop_grid_custom_height", "5"));
        desktopDefaultScreen = Integer.parseInt(sharedPref.getString("desktop_default_screen", "0"));
        desktopShowSearchbar = sharedPref.getBoolean("desktop_show_searchbar", true);
    }
}