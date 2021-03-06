/**   Copyright (C) 2013  Louis Teboul (a.k.a Androguide)
 *
 *    admin@pimpmyrom.org  || louisteboul@gmail.com
 *    http://pimpmyrom.org || http://androguide.fr
 *    71 quai Clémenceau, 69300 Caluire-et-Cuire, FRANCE.
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License along
 *      with this program; if not, write to the Free Software Foundation, Inc.,
 *      51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 **/

package com.androguide.apkreator.pluggable.parsers;

import android.os.AsyncTask;
import android.util.Log;

import com.androguide.apkreator.pluggable.objects.CardPlugin;
import com.androguide.apkreator.pluggable.objects.Config;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PluginParser {
    ArrayList<CardPlugin> cardPlugins;
    ArrayList<Config> configs;
    private CardPlugin cardPlugin;
    private Config config;
    private String text;

    public PluginParser() {
        cardPlugins = new ArrayList<CardPlugin>();
        configs = new ArrayList<Config>();
    }

    public List<CardPlugin> getCardPlugins() {
        return cardPlugins;
    }

    /**
     * @param is An InputStream of the XML file we want to open
     * @return a Collection of CardPlugin Objects, each delimited by <cardPlugin></cardPlugin> tags in the XML
     */
    public List<CardPlugin> parse(InputStream is) {
        try {
            return new AsyncTweaksParsing().execute(is).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<CardPlugin>();
    }

    /**
     * @param is An InputStream of the XML file we want to open
     * @return a Collection of Config Object, each delimited by <cardPlugin></cardPlugin> tags in the XML
     */
    public List<Config> parseConfig(InputStream is) {
        try {
            return new AsyncConfigParsing().execute(is).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<Config>();
    }

    /**
     * Do the parsing in an AsyncTask in order to avoid blocking the UI thread
     * (parsing is an expensive operation)
     */
    private class AsyncTweaksParsing extends AsyncTask<InputStream, Void, ArrayList<CardPlugin>> {

        @Override
        protected ArrayList<CardPlugin> doInBackground(InputStream... streams) {
            XmlPullParserFactory factory;
            XmlPullParser parser;
            try {
                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                parser = factory.newPullParser();
                parser.setInput(streams[0], null);

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = parser.getName();
                    switch (eventType) {

                        // OPENING TAG
                        case XmlPullParser.START_TAG:
                            // <PLUGIN> TAG
                            if (tagName.equalsIgnoreCase("plugin"))
                                cardPlugin = new CardPlugin();

                                // <CARD> TAG
                            else if (tagName.equalsIgnoreCase("card")) {
                                cardPlugin = new CardPlugin();
                                try {
                                    cardPlugin.setType(parser.getAttributeValue(null, "type"));
                                    cardPlugin.setControl(parser.getAttributeValue(null, "control"));
                                    cardPlugin.setProp(parser.getAttributeValue(null, "prop"));
                                    cardPlugin.setTitle(parser.getAttributeValue(null, "title"));
                                    cardPlugin.setDesc(parser.getAttributeValue(null, "description"));
                                    cardPlugin.setUrl(parser.getAttributeValue(null, "url"));
                                    cardPlugin.setBooleanOn(parser.getAttributeValue(null, "on"));
                                    cardPlugin.setBooleanOff(parser.getAttributeValue(null, "off"));
                                    cardPlugin.setButtonText(parser.getAttributeValue(null, "button"));
                                    cardPlugin.setButtonText2(parser.getAttributeValue(null, "button2"));
                                    cardPlugin.setShellCmd(parser.getAttributeValue(null, "command"));
                                    cardPlugin.setShellCmd2(parser.getAttributeValue(null, "command2"));

                                    ArrayList<String> entries = new ArrayList<String>();
                                    ArrayList<String> cmds = new ArrayList<String>();
                                    int values = Integer.parseInt(parser.getAttributeValue(null, "entries-amount"));
                                    for (int i = 0; i < values; i++) {
                                        entries.add(parser.getAttributeValue(null, "entry" + i));
                                        cmds.add(parser.getAttributeValue(null, "command" + i));
                                    }
                                    cardPlugin.setSpinnerEntries(entries);
                                    cardPlugin.setSpinnerCommands(cmds);
                                } catch (Exception e) {
                                    Log.e("Parser", e.getMessage() + "");
                                }
                            }
                            break;

                        // ENCLOSED TEXT
                        case XmlPullParser.TEXT:
                            text = parser.getText();
                            break;

                        // CLOSING TAG
                        case XmlPullParser.END_TAG:
                            if (tagName.equalsIgnoreCase("card"))
                                cardPlugins.add(cardPlugin);
                            else if (tagName.equalsIgnoreCase("title"))
                                cardPlugin.setTitle(text);
                            else if (tagName.equalsIgnoreCase("description"))
                                cardPlugin.setDesc(text);
                            else if (tagName.equalsIgnoreCase("unit"))
                                cardPlugin.setUnit(text);
                            else if (tagName.equalsIgnoreCase("url"))
                                cardPlugin.setUrl(text);
                            else if (tagName.equalsIgnoreCase("path"))
                                cardPlugin.setFilePath(text);
                            else if (tagName.equalsIgnoreCase("button"))
                                cardPlugin.setButtonText(text);
                            else if (tagName.equalsIgnoreCase("command"))
                                cardPlugin.setShellCmd(text);
                            else if (tagName.equalsIgnoreCase("command1"))
                                cardPlugin.setShellCmd(text);
                            else if (tagName.equalsIgnoreCase("command2"))
                                cardPlugin.setShellCmd2(text);
                            else if (tagName.equalsIgnoreCase("stripe-color"))
                                cardPlugin.setStripeColor(text);
                            else if (tagName.equalsIgnoreCase("min-value"))
                                cardPlugin.setMin(Integer.parseInt(text));
                            else if (tagName.equalsIgnoreCase("max-value"))
                                cardPlugin.setMax(Integer.parseInt(text));
                            else if (tagName.equalsIgnoreCase("default-value"))
                                cardPlugin.setDef(Integer.parseInt(text));
                            break;

                        default:
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return cardPlugins;
        }

        protected void onPostExecute(ArrayList<CardPlugin> result) {
            cardPlugins = result;
        }

    }

    /**
     * Do the parsing in an AsyncTask in order to avoid blocking the UI thread (parsing is an expensive operation)
     */
    private class AsyncConfigParsing extends AsyncTask<InputStream, Void, ArrayList<Config>> {

        protected ArrayList<Config> doInBackground(InputStream... streams) {
            XmlPullParserFactory factory;
            XmlPullParser parser;
            try {
                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                parser = factory.newPullParser();

                parser.setInput(streams[0], null);

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = parser.getName();
                    switch (eventType) {

                        /** OPENING TAG */
                        case XmlPullParser.START_TAG:
                            if (tagName.equalsIgnoreCase("config")) {
                                config = new Config();
                                config.setAppName(parser.getAttributeValue(null, "app-name"));
                                config.setAppColor(parser.getAttributeValue(null, "app-color"));
                                config.setWebsite(parser.getAttributeValue(null, "website"));
                                config.setXda(parser.getAttributeValue(null, "xda"));
                                config.setTwitter(parser.getAttributeValue(null, "twitter"));
                                config.setGplus(parser.getAttributeValue(null, "g-plus"));
                                config.setFacebook(parser.getAttributeValue(null, "facebook"));
                                config.setYoutubeUser(parser.getAttributeValue(null, "youtube-user"));
                                config.setWelcomeTitle(parser.getAttributeValue(null, "welcome-title"));
                                config.setWelcomeDesc(parser.getAttributeValue(null, "welcome-desc"));
                                config.setTabsAmount(Integer.parseInt(parser.getAttributeValue(null, "tabs-amount")));
                                config.setDeveloperKey(parser.getAttributeValue(null, "developer-key"));
                                ArrayList<String> tabs = new ArrayList<String>();
                                for (int i = 0; i < config.getTabsAmount(); i++)
                                    tabs.add(i, parser.getAttributeValue(null, "tab" + i));
                                config.setTabs(tabs);
                            } else if (tagName.equalsIgnoreCase("cpu-control")) {
                                config.setCpuControlPos(Integer.parseInt(parser.getAttributeValue(null, "position")));
                            } else if (tagName.equalsIgnoreCase("cordova"))
                                config.setPhoneGapFragmentPos(Integer.parseInt(parser.getAttributeValue(null, "position")));
                            break;

                        /** ENCLOSED TEXT */
                        case XmlPullParser.TEXT:
                            text = parser.getText();
                            break;

                        /** CLOSING TAG */
                        case XmlPullParser.END_TAG:
                            if (text != null)
                                if (tagName.equalsIgnoreCase("config"))
                                    configs.add(config);
                            break;
                        default:
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return configs;
        }

        protected void onPostExecute(ArrayList<Config> result) {
            configs = result;
        }
    }
}