package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TimeStamp extends Module {
   Setting Formats;
   Setting Deco;
   Setting Location;
   Setting Colortype;
   Setting Textcolor;
   Setting BracketColor;

   public TimeStamp() {
      super("TimeStamp", 0, Category.MISC, "TimeStamp");
      this.Formats = Main.setmgr.add(new Setting("formats", this, "H24:mm", new String[]{"H24:mm", "H12:mm", "H12:mm a", "H24:mm:ss", "H12:mm:ss", "H12:mm:ss a"}));
      this.Deco = Main.setmgr.add(new Setting("Deco", this, "bracket", new String[]{"bracket", "square", "curley", "none"}));
      this.Location = Main.setmgr.add(new Setting("Location", this, "Start", new String[]{"Start", "End"}));
   }

   public void setup() {
      ArrayList<String> Color = new ArrayList();
      Color.add("GREEN");
      Color.add("BLACK");
      Color.add("DARK_BLUE");
      Color.add("DARK_GREEN");
      Color.add("DARK_AQUA");
      Color.add("DARK_RED");
      Color.add("DARK_PURPLE");
      Color.add("GOLD");
      Color.add("GRAY");
      Color.add("DARK_GRAY");
      Color.add("BLUE");
      Color.add("AQUA");
      Color.add("RED");
      Color.add("LIGHT_PURPLE");
      Color.add("YELLOW");
      Color.add("WHITE");
      Main.setmgr.add(this.Colortype = new Setting("Time", this, "GREEN", Color));
      Main.setmgr.add(this.Textcolor = new Setting("Text", this, "WHITE", Color));
      Main.setmgr.add(this.BracketColor = new Setting("{C}", this, "WHITE", Color));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketChat) {
         SPacketChat packet2 = (SPacketChat)packet;
         String input = packet2.func_148915_c().func_150260_c();
         if (this.Location.getValString().equalsIgnoreCase("Start")) {
            packet2.field_148919_a = new TextComponentString(this.TmStamp() + " " + TextFormatting.func_96300_b(this.Textcolor.getValString().toLowerCase()) + input + TextFormatting.RESET);
         }

         if (this.Location.getValString().equalsIgnoreCase("End")) {
            packet2.field_148919_a = new TextComponentString(TextFormatting.func_96300_b(this.Textcolor.getValString().toLowerCase()) + input + " " + TextFormatting.RESET + this.TmStamp() + TextFormatting.RESET);
         }
      }

      return true;
   }

   private String TmStamp() {
      String decoLeft = "";
      String decoRight = "";
      if (this.Deco.getValString().equalsIgnoreCase("bracket")) {
         decoLeft = "(";
         decoRight = ")";
      }

      if (this.Deco.getValString().equalsIgnoreCase("square")) {
         decoLeft = "[";
         decoRight = "]";
      }

      if (this.Deco.getValString().equalsIgnoreCase("curley")) {
         decoLeft = "{";
         decoRight = "}";
      }

      String dateFormat = this.Formats.getValString().replace("H24", "k").replace("H12", "h");
      String date = (new SimpleDateFormat(dateFormat)).format(new Date());
      TextComponentString time = new TextComponentString(TextFormatting.func_96300_b(this.BracketColor.getValString().toLowerCase()) + decoLeft + TextFormatting.RESET + TextFormatting.func_96300_b(this.Colortype.getValString().toLowerCase()) + date + TextFormatting.RESET + TextFormatting.func_96300_b(this.BracketColor.getValString().toLowerCase()) + decoRight + TextFormatting.RESET);
      return time.func_150265_g();
   }
}
