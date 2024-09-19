package com.relinwandmire.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import static com.esotericsoftware.minlog.Log.*;

public class Main {
    static Server server;
    static Kryo serverkryo;
    public static List<actvClients> activeClients = new ArrayList<>();

    public Main() {}

    public static void main(String[] args) {
        say("Starting server...");
        Log.set(LEVEL_TRACE);
        server = new Server(9999999, 9999999);
        serverkryo = server.getKryo();
        serverkryo.register(TextChat.class);
        serverkryo.register(layerhistory.class);
        serverkryo.register(PlayerState.class);
        serverkryo.register(command.class);
        runServer(45372);
        say("Waiting...");
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof TextChat) {
                    TextChat request = (TextChat) object;
                    Main.broadcast(request.text);
                    Main.say(request.text);
                } else if (object instanceof command) {
                    command cmd = (command) object;
                    command cc;
                    Iterator<actvClients> var14;
                    Iterator<actvClients> var15;
                    actvClients av;
                    actvClients avx;
                    switch (cmd.command) {
                        case "registerID":
                            actvClients acv = new actvClients();
                            acv.id = cmd.data;
                            acv.room = "";
                            Main.activeClients.add(acv);
                            Main.say("[S] Registration requested by: " + acv.id);
                            Main.activeClients.add(acv);
                            cc = new command();
                            cc.from = acv.id;
                            cc.command = "registered";
                            connection.sendTCP(cc);
                            break;
                        case "createRoom":
                            boolean roomok = true;
                            var14 = Main.activeClients.iterator();

                            while(var14.hasNext()) {
                                av = var14.next();
                                if (av.creator && av.room.equalsIgnoreCase(cmd.room)) {
                                    roomok = false;
                                }
                            }

                            if (roomok) {
                                var14 = Main.activeClients.iterator();

                                while(var14.hasNext()) {
                                    av = var14.next();
                                    if (av.id.equalsIgnoreCase(cmd.from)) {
                                        av.room = cmd.room;
                                        av.creator = true;
                                    }
                                }

                                cc = new command();
                                cc.command = "roomCreateOK";
                                connection.sendTCP(cc);
                                Main.say("[S] Created room: " + cmd.room);
                            } else {
                                cc = new command();
                                cc.command = "roomCreateFailed";
                                connection.sendTCP(cc);
                                Main.say("[S] Room not created: " + cmd.room);
                            }
                            break;
                        case "destroyRoom":
                            var14 = Main.activeClients.iterator();

                            while(var14.hasNext()) {
                                av = var14.next();
                                if (av.room.equalsIgnoreCase(cmd.room)) {
                                    av.room = "";
                                    av.creator = false;
                                }
                            }

                            cc = new command();
                            cc.command = "roomDestroyed";
                            cc.room = cmd.room;
                            Main.server.sendToAllTCP(cc);
                            Main.say("[S] Broadcasted room destruction: " + cmd.room);
                            break;
                        case "joinRequest":
                            boolean isavail = false;
                            var15 = Main.activeClients.iterator();

                            while(var15.hasNext()) {
                                avx = var15.next();
                                if (avx.room.equalsIgnoreCase(cmd.room) && avx.creator) {
                                    isavail = true;
                                }
                            }

                            if (isavail) {
                                var15 = Main.activeClients.iterator();

                                while(var15.hasNext()) {
                                    avx = var15.next();
                                    if (avx.id.equalsIgnoreCase(cmd.from)) {
                                        avx.room = cmd.room;
                                    }
                                }

                                cc = new command();
                                cc.command = "joinRequestAccepted";
                                cc.room = cmd.room;
                                connection.sendTCP(cc);
                                Main.say("[S] Join Request accepted for: " + cmd.from + " @ room:" + cmd.room);
                                Main.say("[SB] broadcasting join information");
                                cc = new command();
                                cc.command = "joinInformation";
                                cc.room = cmd.room;
                                cc.data = cmd.from;
                                Main.server.sendToAllTCP(cc);
                            } else {
                                cc = new command();
                                cc.command = "joinRequestRejected";
                                cc.room = cmd.room;
                                connection.sendTCP(cc);
                                Main.say("[S] Join Request rejected for: " + cmd.from + " @ room:" + cmd.room);
                            }
                            break;
                        case "leaveRequest":
                            var15 = Main.activeClients.iterator();

                            while(var15.hasNext()) {
                                avx = var15.next();
                                if (avx.id.equalsIgnoreCase(cmd.from)) {
                                    avx.room = "";
                                }
                            }

                            Main.say("[S] left room for: " + cmd.from + " @ " + cmd.room);
                            cc = new command();
                            cc.command = "leaveRequestAccepted";
                            cc.room = cmd.room;
                            connection.sendTCP(cc);
                            Main.say("[SB] broadcasting leave information");
                            cc = new command();
                            cc.command = "leaveInformation";
                            cc.room = cmd.room;
                            cc.data = cmd.from;
                            Main.server.sendToAllTCP(cc);
                            break;
                        case "startdata":
                        case "data":
                        case "startDataAll":
                        case "dataAll":
                        case "draw":
                            Main.server.sendToAllTCP(cmd);
                            break;
                        case "readThisBoy":
                            cc = new command();
                            cc.command = "mapInformation";
                            cc.from = cmd.from;
                            cc.room = cmd.room;
                            Main.server.sendToAllTCP(cc);
                            Main.say("[SB] broadcasting map information");
                            break;
                        case "allReadThis":
                            cc = new command();
                            cc.command = "mapInformationAll";
                            cc.from = cmd.from;
                            cc.room = cmd.room;
                            Main.server.sendToAllTCP(cc);
                            Main.say("[SB] broadcasting open map information");
                            break;
                        case "disconnect":
                            Main.say("[S] Disconnect Request...");
                            int flag = -1;

                            for(int i = 0; i < Main.activeClients.size(); ++i) {
                                if (Main.activeClients.get(i).id.equalsIgnoreCase(cmd.from)) {
                                    flag = i;
                                }
                            }

                            if (flag != -1) {
                                Main.activeClients.remove(flag);
                                Main.say("[S] Client erased.");
                            }
                    }
                } else if (object instanceof layerhistory) {
                    layerhistory response = (layerhistory)object;
                    Main.say("received from client");
                    Main.pushdata(response);
                }

            }
        });
    }

    private static void broadcast(String text) {
        try {
            TextChat sr = new TextChat();
            sr.text = text;
            server.sendToAllTCP(sr);
        } catch (Exception ignored) {
        }

    }

    private static void pushdata(packet lh) {
        try {
            server.sendToAllTCP(lh);
        } catch (Exception var2) {
            Exception e = var2;
            e.printStackTrace();
        }

    }

    private static void runServer(int port) {
        try {
            if (port <= 0) {
                say("Please enter port number.");
                return;
            }

            server.start();

            /**
             *  UDP 和 NT自身的 MyGDXGame.java runClient方法中的 必须一致 否则就无法连接
              */
            server.bind(port, 23281);
            say("Server started on port :" + port);
            say("Listening on: " + getLocalIpAddress());
        } catch (Exception var2) {
            Exception e = var2;
            e.printStackTrace();
        }

    }

    private static void say(String text) {
        System.out.println(text);
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException var4) {
            SocketException ex = var4;
            ex.printStackTrace();
        }

        return null;
    }
}
