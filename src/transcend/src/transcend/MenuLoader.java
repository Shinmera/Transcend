/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend;

import java.io.File;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import gui.*;
import static transcend.MainFrame.*;
import static org.lwjgl.opengl.GL11.*;

public class MenuLoader extends LoadHelper{
    public void load(){
        GPanel p_editor = new GPanel(0,10,130,DISPLAY_HEIGHT-20){
            public void paint(){if(editor.getActive())super.paint();}
        };
        p_editor.setBackground(new Color(255,255,255,150));

        final GLabel l_block = new GLabel(editor.getItemName(editor.getItem()));
        GButton b_quit = new GButton("Quit"){
            public void onRelease(){destroy();}
        };
        GButton b_editor = new GButton("Toggle Editor"){
            public void onRelease(){if(editor.getActive()){
                editor.setActive(false);this.setBackground(Color.red);
            }else{
                editor.setActive(true);this.setBackground(Color.green);
            }}
        };
        GButton b_settings = new GButton("Settings"){
            public void onRelease(){
                if(DisplayModeChooser.showDialog("Display Mode")){
                    CONST.loadRegistry();
                    DISPLAY_WIDTH=Display.getDisplayMode().getWidth();
                    DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();
                    fps=CONST.gInteger("FPS");
                    ACSIZE=CONST.gInteger("ANTIALIAS");
                    hid.setBounds(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
                    menu.setBounds(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
                }
            }
        };
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
        GLabel l_speed = new GLabel(""){
            public void paint(){
                if(!isVisible())return;
                getForeground().bind();
                getFont().drawString(10,DISPLAY_HEIGHT-20,player.getInfo(), 1,1, TrueTypeFont.ALIGN_LEFT);
                glBindTexture(GL_TEXTURE_2D, 0); //release
            }
        };
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
        final GTextField t_file = new GTextField("test.tw");
        GButton b_save = new GButton("Save"){
            public void onRelease(){
                worldLoader.saveWorld(new File("world"+File.separator+t_file.getText()));
            }
        };
        GButton b_load = new GButton("Load"){
            public void onRelease(){
                worldLoader.loadWorld(new File("world"+File.separator+t_file.getText()));
            }
        };
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

        b_editor.setBackground(Color.red);
        b_quit.setBounds(10, 10, 100, 30);
        b_editor.setBounds(10,80,100, 30);
        b_settings.setBounds(10,45,100,30);
        GImage i_logo = new GImage("logo.png");
        i_logo.setBounds(DISPLAY_WIDTH/2-250,DISPLAY_HEIGHT-106-50,500,106);


        b_save.setBounds(10,10,100,30);
        b_load.setBounds(10,45,100,30);
        t_file.setBounds(10,80,100,15);

        l_zoomdesc.setBounds(10,p_editor.getHeight()-15-10,100,15);
        b_zoomin.setBounds(10,p_editor.getHeight()-15-30,15,15);
        b_zoomout.setBounds(30,p_editor.getHeight()-15-30,15,15);
        l_zoom.setBounds(50,p_editor.getHeight()-15-30,60,15);

        l_layerdesc.setBounds(10,p_editor.getHeight()-15-50,100,15);
        b_layerp.setBounds(10,p_editor.getHeight()-15-70,15,15);
        b_layerm.setBounds(30,p_editor.getHeight()-15-70,15,15);
        l_layer.setBounds(50,p_editor.getHeight()-15-70,60,15);

        l_blockdesc.setBounds(10,p_editor.getHeight()-15-90,100,15);
        b_prev.setBounds(10,p_editor.getHeight()-15-110,15,15);
        b_next.setBounds(30,p_editor.getHeight()-15-110,15,15);
        l_block.setBounds(50,p_editor.getHeight()-15-110,60,15);

        l_tilesdesc.setBounds(10,p_editor.getHeight()-15-130,100,15);
        t_tiles.setBounds(10,p_editor.getHeight()-15-150,100,15);

        l_argsdesc.setBounds(10,p_editor.getHeight()-15-170,100,15);
        t_args.setBounds(10,p_editor.getHeight()-15-230,100,55);

        menu.add(b_quit);
        menu.add(b_editor);
        menu.add(b_settings);
        menu.add(i_logo);
        p_editor.add(b_save);
        p_editor.add(b_load);
        p_editor.add(t_file);
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
        p_editor.add(l_blockdesc);
        p_editor.add(l_layerdesc);
        p_editor.add(l_zoomdesc);
        p_editor.add(l_argsdesc);
        p_editor.add(t_args,"args");
        hid.add(editor,"editor");
        hid.add(p_editor,"p_editor");
        hid.add(l_speed);
        hid.setVisible(true);
    }
}
