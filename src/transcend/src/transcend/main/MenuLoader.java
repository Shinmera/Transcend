/**********************\
  file: MenuLoader.java
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.main;

import java.awt.Font;
import java.io.File;
import org.newdawn.slick.Color;
import transcend.gui.*;
import static transcend.main.MainFrame.*;

public class MenuLoader extends LoadHelper{
    public void load(){
        constructMainPane();
        constructPausePane();
        constructEditorPane();
        constructLoadPane();
        constructSettingsPane();
        constructHelpPane();
        constructHUDPane();
        GImage i_logo = new GImage("logo");
        i_logo.setBounds(DISPLAY_WIDTH/2-DISPLAY_WIDTH/4,DISPLAY_HEIGHT-DISPLAY_HEIGHT/4,DISPLAY_WIDTH/2,DISPLAY_HEIGHT/4);
        menu.add(i_logo);
        hud.setVisible(true);
        
        editor.setActive(false);
        menu.setVisible(false);
    }

    private void constructMainPane(){
        int panel_width=(int)(DISPLAY_WIDTH/100.0*25.0);
        GPanel p_main = new GPanel(DISPLAY_WIDTH/2-panel_width/2,0,panel_width,DISPLAY_HEIGHT-DISPLAY_HEIGHT/4);

        GButton b_quit = new GButton("Quit"){
            public void onRelease(){MainFrame.frame.setCloseRequested(true);}
        };

        GButton b_start = new GButton("Start Game"){
            public void onRelease(){
                menu.get("p_main").setVisible(false);
                loader.setHelper(new LoadHelper(){public void load(){
                    world.loadWorld(new File("world"+File.separator+"intro.tw"));
                    menu.setVisible(false);
                    hud.setVisible(true);
                    unpause();
                }});
                loader.start("Loading world...");
            }
        };

        GButton b_load = new GButton("Load Game"){
            public void onRelease(){
                menu.get("p_main").setVisible(false);
                menu.get("p_load").setVisible(true);
            }
        };

        GButton b_help = new GButton("Help"){
            public void onRelease(){
                menu.get("p_main").setVisible(false);
                menu.get("p_help").setVisible(true);
            }
        };

        GButton b_settings = new GButton("Settings"){
            public void onRelease(){
                menu.get("p_main").setVisible(false);
                menu.get("p_settings").setVisible(true);
            }
        };

        b_quit.autoBounds(p_main, 0, 10, panel_width, 30);
        b_start.autoBounds(p_main, 0, p_main.getHeight()-40, panel_width, 30);
        b_load.autoBounds(p_main, 0, p_main.getHeight()-80, panel_width, 30);
        b_help.autoBounds(p_main, 0, p_main.getHeight()-130, panel_width, 30);
        b_settings.autoBounds(p_main, 0, p_main.getHeight()-170, panel_width, 30);

        p_main.setBackground(new Color(255,255,255,150));
        p_main.add(b_quit);
        p_main.add(b_load);
        p_main.add(b_settings);
        p_main.add(b_help);
        p_main.add(b_start);
        p_main.setVisible(false);

        menu.add(p_main,"p_main");
    }

    private void constructSettingsPane(){
        int panel_width=(int)(DISPLAY_WIDTH/100.0*50.0);
        GPanel p_settings  = new GPanel(DISPLAY_WIDTH/2-panel_width/2,0,panel_width,DISPLAY_HEIGHT-DISPLAY_HEIGHT/4);

        GButton b_return = new GButton("Return"){
            public void onRelease(){
                menu.get("p_main").setVisible(true);
                menu.get("p_settings").setVisible(false);
            }
        };

        b_return.autoBounds(p_settings, 0, 10, panel_width, 30);

        p_settings.setBackground(new Color(255,255,255,150));
        p_settings.add(b_return);

        menu.add(p_settings,"p_settings");
    }

    private void constructHelpPane(){
        int panel_width=(int)(DISPLAY_WIDTH/100.0*50.0);
        GPanel p_help  = new GPanel(DISPLAY_WIDTH/2-panel_width/2,0,panel_width,DISPLAY_HEIGHT-DISPLAY_HEIGHT/4);

        GButton b_return = new GButton("Return"){
            public void onRelease(){
                menu.get("p_main").setVisible(true);
                menu.get("p_help").setVisible(false);
            }
        };
        GLabel l_help = new GLabel("Global controls:\nDelete or Escape: Bring up pause menu\n\n"
                                 + "Player controls:\nA: Walk left\nD: Walk right\nSpace: Jump\nShift: Run\nUp: Attack/Use\n1-5: Switch form\n\n"
                                 + "Editor controls:\nR: Return Player to 0,0");
        l_help.setAlign(GLabel.ALIGN_LEFT);
        
        b_return.autoBounds(p_help, 0, 10, panel_width, 30);
        l_help.autoBounds(p_help, 10, 50, panel_width-20,(DISPLAY_HEIGHT-DISPLAY_HEIGHT/4)-60);

        p_help.setBackground(new Color(255,255,255,150));
        p_help.add(b_return);
        p_help.add(l_help);

        menu.add(p_help,"p_help");
    }

    private void constructPausePane(){
        int panel_width=(int)(DISPLAY_WIDTH/100.0*25.0);
        GPanel p_pause  = new GPanel(DISPLAY_WIDTH/2-panel_width/2,0,panel_width,DISPLAY_HEIGHT-DISPLAY_HEIGHT/4);
        GButton b_editor = new GButton("Toggle Editor"){
            public void onRelease(){if(editor.getActive()){
                editor.setActive(false);
                this.setBackground(Color.red);
            }else{
                editor.setActive(true);
                this.setBackground(Color.green);
            }}
        };
        //LOAD BUTTON
        GButton b_load = new GButton("Load Game"){
            public void onRelease(){
                menu.get("p_pause").setVisible(false);
                menu.get("p_load").setVisible(true);
            }
        };
        //QUIT BUTTON (Main Menu)
        GButton b_quit = new GButton("Quit to Menu"){
            public void onRelease(){
                MainFrame.loader.setHelper(new LoadHelper(){
                    public void load() {MainFrame.world.loadWorld(MainFrame.fileStorage.getFile("menu.tw"));}
                });
                MainFrame.loader.start("Loading menu...");
                editor.setVisible(false);
                editor.setActive(false);
                hud.setVisible(false);
                menu.get("p_pause").setVisible(false);
                menu.get("p_main").setVisible(true);
            }
        };
        //RETURN BUTTON
        GButton b_return = new GButton("Return to Game"){
            public void onRelease(){
                menu.setVisible(false);
                hud.setVisible(true);
                unpause();
            }
        };

        b_editor.setBackground(Color.green);
        b_editor.autoBounds(p_pause, 0, 100, panel_width, 30);
        b_return.autoBounds(p_pause, 0, p_pause.getHeight()-40, panel_width, 30);
        b_load.autoBounds(p_pause, 0,   p_pause.getHeight()-80, panel_width, 30);
        b_quit.autoBounds(p_pause, 0,   p_pause.getHeight()-120, panel_width, 30);

        p_pause.setBackground(new Color(255,255,255,150));
        p_pause.add(b_editor);
        p_pause.add(b_return);
        p_pause.add(b_load);
        p_pause.add(b_quit);

        menu.add(p_pause,"p_pause");
    }

    private void constructLoadPane(){
        int panel_width=(int)(DISPLAY_WIDTH/100.0*50.0);
        GPanel p_load  = new GPanel(DISPLAY_WIDTH/2-panel_width/2,0,panel_width,DISPLAY_HEIGHT-DISPLAY_HEIGHT/4){
            public void setVisible(boolean visible){
                super.setVisible(visible);
                ((GList)this.get("list")).clear();
                File[] saves = new File(basedir,"world"+File.separator+"save"+File.separator).listFiles();
                if(saves!=null){
                    for(int i=0;i<saves.length;i++){
                        if(!saves[i].isDirectory())((GList)this.get("list")).addListElement(saves[i].getName());
                    }
                }
            }
        };


        GList l_loads = new GList(){};
        GButton b_load = new GButton("Load"){
            public void onRelease(){
                final String name = ((GList)((GPanel)menu.get("p_load")).get("list")).getSelected();
                loader.setHelper(new LoadHelper(){public void load(){
                    world.loadGame(MainFrame.fileStorage.getFile("save/"+name));
                    menu.setVisible(false);
                    hud.setVisible(true);
                    unpause();
                }});
                loader.start("Loading world...");
            }
        };
        GButton b_return = new GButton("Return"){
            public void onRelease(){
                menu.get("p_pause").setVisible(true);
                menu.get("p_load").setVisible(false);
            }
        };
        
        l_loads.autoBounds(p_load, 0, p_load.getHeight()-(DISPLAY_HEIGHT-DISPLAY_HEIGHT/4-50), panel_width, DISPLAY_HEIGHT-DISPLAY_HEIGHT/4-50);
        b_load.autoBounds(p_load, 0, 10, panel_width/2-5, 30);
        b_return.autoBounds(p_load, panel_width/2+5, 10, panel_width/2-5, 30);

        p_load.setBackground(new Color(255,255,255,150));
        p_load.add(l_loads,"list");
        p_load.add(b_load);
        p_load.add(b_return);

        menu.add(p_load,"p_load");
    }


    private void constructEditorPane(){
        GPanel p_editor = new GPanel(0,10,130,DISPLAY_HEIGHT-20){
            public void paint(){if(editor.getActive())super.paint();}
            public void setVisible(boolean visible){
                super.setVisible(visible);
                GTextField file = (GTextField)get("t_file");
                if(file!=null&&world.isLoaded()){
                    file.setText(world.getLoaded().getName());
                }
            }
        };
        p_editor.setBackground(new Color(255,255,255,150));

        final GLabel l_block = new GLabel(editor.getItemName(editor.getItem()));

        GButton b_prev = new GButton("<"){
            public void onRelease(){
                editor.setItem(editor.getItem()-1);
                if(editor.getItem()<0)editor.setItem(editor.getItemCount()-1);
                l_block.setText(editor.getItemName(editor.getItem()));
            }
        };
        GButton b_next = new GButton(">"){
            public void onRelease(){
                editor.setItem(editor.getItem()+1);
                if(editor.getItem()==editor.getItemCount())editor.setItem(0);
                l_block.setText(editor.getItemName(editor.getItem()));
            }
        };
        GTextField t_tiles = new GTextField(editor.getTilesize()+""){
            public void onConfirm(){
                editor.setTilesize(Integer.parseInt(getText()));
            }
        };
        GLabel l_speed = new GLabel("");
        final GLabel l_zoom = new GLabel(camera.getZoom()+"");
        GButton b_zoomin = new GButton("+"){
            public void onHold(){
                if(camera.getZoom()<5)camera.setZoom(camera.getZoom()+0.01);
                l_zoom.setText(Math.round(camera.getZoom()*100)/100.0+"");
            }
        };
        GButton b_zoomout = new GButton("-"){
            public void onHold(){
                if(camera.getZoom()>0.05)camera.setZoom(camera.getZoom()-0.01);
                l_zoom.setText(Math.round(camera.getZoom()*100)/100.0+"");
            }
        };
        final GLabel l_layer = new GLabel(editor.getCurLayer()+"");
        GButton b_layerp = new GButton("+"){
            public void onPress(){
                editor.setCurLayer(editor.getCurLayer()+1);
                if(editor.getCurLayer()>5)editor.setCurLayer(5);
                l_layer.setText(editor.getCurLayer()+"");
            }
        };
        GButton b_layerm = new GButton("-"){
            public void onPress(){
                editor.setCurLayer(editor.getCurLayer()-1);
                if(editor.getCurLayer()<-5)editor.setCurLayer(-5);
                l_layer.setText(editor.getCurLayer()+"");
            }
        };
        final GTextArea t_args = new GTextArea("");
        final GTextField t_file = new GTextField("");
        GButton b_save = new GButton("Save"){
            public void onRelease(){
                world.saveWorld(new File("world"+File.separator+t_file.getText()));
            }
        };
        GButton b_load = new GButton("Load"){
            public void onRelease(){
                MainFrame.loader.setHelper(new LoadHelper(){
                    public void load() {MainFrame.world.loadWorld(MainFrame.fileStorage.getFile(t_file.getText()));}
                });
                MainFrame.loader.start("Loading world...");
            }
        };
        GRadioButton r_blocks = new GRadioButton(p_editor,"Blocks"){
            public void onPress(){
                super.onPress();
                MainFrame.editor.setMode(Editor.MODE_BLOCKS);
            }
        };
        GRadioButton r_entities = new GRadioButton(p_editor,"Entities"){
            public void onPress(){
                super.onPress();
                MainFrame.editor.setMode(Editor.MODE_ENTITIES);
            }
        };
        GCheckBox c_entities = new GCheckBox(p_editor,"Entities"){
            public void onRelease(){
                super.onRelease();
                MainFrame.editor.setRemoveEntities(this.isActivated());
            }
        };
        GCheckBox c_blocks = new GCheckBox(p_editor,"Blocks"){
            public void onRelease(){
                super.onRelease();
                MainFrame.editor.setRemoveBlocks(this.isActivated());
            }
        };
        GCheckBox c_tiles = new GCheckBox(p_editor,"Tiles"){
            public void onRelease(){
                super.onRelease();
                MainFrame.editor.setRemoveTiles(this.isActivated());
            }
        };
        GCheckBox c_layerlock = new GCheckBox(p_editor,"Lock layer"){
            public void onRelease(){
                super.onRelease();
                MainFrame.editor.setLayerLocked(this.isActivated());
            }
        };
        if(MainFrame.editor.getMode()==Editor.MODE_BLOCKS)r_blocks.setActivated(true);
        else r_entities.setActivated(true);
        c_entities.setActivated(true);
        c_blocks.setActivated(true);
        c_tiles.setActivated(true);

        GLabel l_blockdesc = new GLabel("Block:",GLabel.ALIGN_LEFT);
        l_blockdesc.setBorder(new Color(0,0,0,0),0);
        l_blockdesc.setBackground(new Color(1,1,1,0.5f));
        GLabel l_layerdesc = new GLabel("Layer:",GLabel.ALIGN_LEFT);
        l_layerdesc.setBorder(new Color(0,0,0,0),0);
        l_layerdesc.setBackground(new Color(1,1,1,0.5f));
        GLabel l_zoomdesc = new GLabel("Zoom:",GLabel.ALIGN_LEFT);
        l_zoomdesc.setBorder(new Color(0,0,0,0),0);
        l_zoomdesc.setBackground(new Color(1,1,1,0.5f));
        GLabel l_tilesdesc = new GLabel("Tilesize:",GLabel.ALIGN_LEFT);
        l_tilesdesc.setBorder(new Color(0,0,0,0),0);
        l_tilesdesc.setBackground(new Color(1,1,1,0.5f));
        GLabel l_argsdesc = new GLabel("Arguments:",GLabel.ALIGN_LEFT);
        l_argsdesc.setBorder(new Color(0,0,0,0),0);
        l_argsdesc.setBackground(new Color(1,1,1,0.5f));
        GLabel l_removedesc = new GLabel("Removable:",GLabel.ALIGN_LEFT);
        l_removedesc.setBorder(new Color(0,0,0,0),0);
        l_removedesc.setBackground(new Color(1,1,1,0.5f));

        l_speed.setBounds(10,DISPLAY_HEIGHT-20,400,15);
        l_speed.setBorder(new Color(0,0,0,0),0);
        l_speed.setBackground(new Color(0,0,0,0));

        b_save.setBounds(10,10,100,30);
        b_load.setBounds(10,45,100,30);
        t_file.setBounds(10,80,100,15);

        r_blocks.setBounds(20,p_editor.getHeight()-15-10,100,15);
        r_entities.setBounds(80,p_editor.getHeight()-15-10,100,15);

        l_zoomdesc.setBounds(10,p_editor.getHeight()-15-30,100,15);
        b_zoomin.setBounds(10,p_editor.getHeight()-15-50,15,15);
        b_zoomout.setBounds(30,p_editor.getHeight()-15-50,15,15);
        l_zoom.setBounds(50,p_editor.getHeight()-15-50,60,15);

        l_layerdesc.setBounds(10,p_editor.getHeight()-15-70,100,15);
        b_layerp.setBounds(10,p_editor.getHeight()-15-90,15,15);
        b_layerm.setBounds(30,p_editor.getHeight()-15-90,15,15);
        l_layer.setBounds(50,p_editor.getHeight()-15-90,60,15);

        l_blockdesc.setBounds(10,p_editor.getHeight()-15-110,100,15);
        b_prev.setBounds(10,p_editor.getHeight()-15-130,15,15);
        b_next.setBounds(30,p_editor.getHeight()-15-130,15,15);
        l_block.setBounds(50,p_editor.getHeight()-15-130,60,15);

        l_tilesdesc.setBounds(10,p_editor.getHeight()-15-150,100,15);
        t_tiles.setBounds(10,p_editor.getHeight()-15-170,100,15);

        l_argsdesc.setBounds(10,p_editor.getHeight()-15-190,100,15);
        t_args.setBounds(10,p_editor.getHeight()-15-250,100,55);

        l_removedesc.setBounds(10,p_editor.getHeight()-15-270,100,15);
        c_entities.setBounds(10,p_editor.getHeight()-15-290,100,15);
        c_blocks.setBounds(10,p_editor.getHeight()-15-310,100,15);
        c_tiles.setBounds(10,p_editor.getHeight()-15-330,100,15);
        
        c_layerlock.setBounds(10,p_editor.getHeight()-15-350,100,15);


        p_editor.add(b_save);
        p_editor.add(b_load);
        p_editor.add(t_file,"t_file");
        p_editor.add(t_tiles);
        p_editor.add(l_tilesdesc);
        p_editor.add(b_prev);
        p_editor.add(b_next);
        p_editor.add(l_block);
        p_editor.add(b_zoomin);
        p_editor.add(b_zoomout);
        p_editor.add(l_zoom);
        p_editor.add(b_layerp);
        p_editor.add(b_layerm);
        p_editor.add(l_layer);
        p_editor.add(c_entities);
        p_editor.add(c_blocks);
        p_editor.add(c_tiles);
        p_editor.add(c_layerlock);
        p_editor.add(l_blockdesc);
        p_editor.add(l_layerdesc);
        p_editor.add(l_zoomdesc);
        p_editor.add(l_argsdesc);
        p_editor.add(l_removedesc);
        p_editor.add(t_args,"args");
        p_editor.add(r_blocks,"r_blocks");
        p_editor.add(r_entities,"r_entities");
        hud.add(editor,"editor");
        hud.add(p_editor,"p_editor");
        hud.add(l_speed,"hidinfo");
    }

    public void constructHUDPane(){
        HPowerBar powerBar = new HPowerBar();
        powerBar.setBounds(DISPLAY_WIDTH-400,10,512,64);
        GLabel score = new GLabel(){
            public void paint() {
                setText("Score: "+player.getScore());
                super.paint();
            }
        };
        score.setFont(new Font("Arial",Font.BOLD,18));
        score.setAlign(GLabel.ALIGN_LEFT);
        score.setBackground(new Color(0,0,0,0));
        score.setForeground(Color.white);
        score.setBorder(new Color(0,0,0,0), 0);
        score.setBounds(20, DISPLAY_HEIGHT-30-20, 200, 30);
        HFormSelector formsel = new HFormSelector();
        
        MainFrame.gameLog.setBounds(DISPLAY_WIDTH-500, 0, 500, 200);
        
        hud.add(formsel,"formselector");
        hud.add(score,"score");
        hud.add(powerBar,"powerbar");
        hud.add(MainFrame.gameLog,"gamelog");
    }
}
