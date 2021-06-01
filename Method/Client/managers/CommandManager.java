package Method.Client.managers;

import Method.Client.module.command.Author;
import Method.Client.module.command.BedCoords;
import Method.Client.module.command.ClearChat;
import Method.Client.module.command.Command;
import Method.Client.module.command.Drop;
import Method.Client.module.command.Effect;
import Method.Client.module.command.FakePlayer;
import Method.Client.module.command.Friend;
import Method.Client.module.command.Give;
import Method.Client.module.command.Hat;
import Method.Client.module.command.Hclip;
import Method.Client.module.command.Head;
import Method.Client.module.command.Help;
import Method.Client.module.command.Login;
import Method.Client.module.command.Lore;
import Method.Client.module.command.Nbt;
import Method.Client.module.command.OpenDir;
import Method.Client.module.command.OpenFolder;
import Method.Client.module.command.OpenGui;
import Method.Client.module.command.Peek;
import Method.Client.module.command.Pitch;
import Method.Client.module.command.PlayerFinder;
import Method.Client.module.command.PrefixChange;
import Method.Client.module.command.Profile;
import Method.Client.module.command.Rename;
import Method.Client.module.command.Repair;
import Method.Client.module.command.Reset;
import Method.Client.module.command.ResetGui;
import Method.Client.module.command.Say;
import Method.Client.module.command.StackSize;
import Method.Client.module.command.Tp;
import Method.Client.module.command.UsernameHistory;
import Method.Client.module.command.VClip;
import Method.Client.module.command.Vanish;
import Method.Client.module.command.WorldSeed;
import Method.Client.module.command.Yaw;
import Method.Client.utils.visual.ChatUtils;
import java.util.ArrayList;
import java.util.Iterator;

public class CommandManager {
   public static ArrayList<Command> commands = new ArrayList();
   private static volatile CommandManager instance;
   public static char cmdPrefix = '@';

   public CommandManager() {
      this.addCommands();
   }

   public void addCommands() {
      commands.add(new Help());
      commands.add(new VClip());
      commands.add(new OpenFolder());
      commands.add(new Login());
      commands.add(new FakePlayer());
      commands.add(new UsernameHistory());
      commands.add(new Say());
      commands.add(new PrefixChange());
      commands.add(new OpenGui());
      commands.add(new Effect());
      commands.add(new PlayerFinder());
      commands.add(new WorldSeed());
      commands.add(new Friend());
      commands.add(new ClearChat());
      commands.add(new OpenDir());
      commands.add(new Author());
      commands.add(new ResetGui());
      commands.add(new Yaw());
      commands.add(new Pitch());
      commands.add(new BedCoords());
      commands.add(new Drop());
      commands.add(new Peek());
      commands.add(new Vanish());
      commands.add(new StackSize());
      commands.add(new Hclip());
      commands.add(new Reset());
      commands.add(new Give());
      commands.add(new Hat());
      commands.add(new Head());
      commands.add(new Lore());
      commands.add(new Nbt());
      commands.add(new Rename());
      commands.add(new Repair());
      commands.add(new Tp());
      commands.add(new Profile());
   }

   public void runCommands(String s) {
      String readString = s.trim().substring(Character.toString(cmdPrefix).length()).trim();
      boolean commandResolved = false;
      boolean hasArgs = readString.trim().contains(" ");
      String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
      String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];
      Iterator var7 = commands.iterator();

      while(var7.hasNext()) {
         Command command = (Command)var7.next();
         if (command.getCommand().trim().equalsIgnoreCase(commandName.trim())) {
            command.runCommand(readString, args);
            commandResolved = true;
            break;
         }
      }

      if (!commandResolved) {
         ChatUtils.error("Cannot resolve internal command: §c" + commandName);
      }

   }

   public static CommandManager getInstance() {
      if (instance == null) {
         instance = new CommandManager();
      }

      return instance;
   }
}
