package Method.Client.utils.Screens.Custom.Packet;

import Method.Client.Main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.input.Mouse;

public final class AntiPacketGui extends GuiScreen {
   private static AntiPacketGui.ListGui listGui;
   private AntiPacketGui.ListGui AllMobs;
   private GuiTextField MobFieldName;
   private GuiButton addButton;
   private GuiButton removeButton;
   private GuiButton doneButton;
   private String PacketToAdd;
   private String PacketToRemove;
   public static ArrayList<String> Packets = new ArrayList();
   public static ArrayList<String> AllPackets = new ArrayList();
   public static ArrayList<AntiPacketPacket> ListOfPackets = new ArrayList();

   public boolean func_73868_f() {
      return false;
   }

   public static List<String> removePackets(List<String> input) {
      AllPackets.clear();
      Iterator var1 = ListOfPackets.iterator();

      while(var1.hasNext()) {
         AntiPacketPacket listOfPacket = (AntiPacketPacket)var1.next();
         if (!input.contains(listOfPacket.packet.getClass().getSimpleName())) {
            AllPackets.add(listOfPacket.packet.getClass().getSimpleName());
         }
      }

      return AllPackets;
   }

   public static List<String> PacketSearch(String text) {
      ArrayList<String> Temp = new ArrayList();
      Iterator var2 = ListOfPackets.iterator();

      while(var2.hasNext()) {
         AntiPacketPacket listOfPacket = (AntiPacketPacket)var2.next();
         String s = listOfPacket.packet.getClass().getSimpleName();
         if (listOfPacket.packet.getClass().getSimpleName().toLowerCase().contains(text.toLowerCase())) {
            Temp.add(s);
         }
      }

      return Temp;
   }

   public void func_73866_w_() {
      this.ListSetup();
      listGui = new AntiPacketGui.ListGui(this.field_146297_k, this, Packets, this.field_146294_l - this.field_146294_l / 3, 0);
      this.AllMobs = new AntiPacketGui.ListGui(this.field_146297_k, this, removePackets(Packets), 0, 0);
      this.MobFieldName = new GuiTextField(1, this.field_146297_k.field_71466_p, 64, this.field_146295_m - 55, 150, 18);
      this.field_146292_n.add(this.addButton = new GuiButton(0, 214, this.field_146295_m - 56, 30, 20, "Add"));
      this.field_146292_n.add(this.removeButton = new GuiButton(1, this.field_146294_l - 300, this.field_146295_m - 56, 100, 20, "Remove Selected"));
      this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 108, 8, 100, 20, "Remove All"));
      this.field_146292_n.add(new GuiButton(20, this.field_146294_l - 308, 8, 100, 20, "Add Server"));
      this.field_146292_n.add(new GuiButton(40, this.field_146294_l - 208, 8, 100, 20, "Add Client"));
      this.field_146292_n.add(this.doneButton = new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m - 28, "Done"));
   }

   public static ArrayList<AntiPacketPacket> GetPackets() {
      ArrayList<AntiPacketPacket> list = new ArrayList();
      Iterator var1 = ListOfPackets.iterator();

      while(true) {
         while(var1.hasNext()) {
            AntiPacketPacket listOfPacket = (AntiPacketPacket)var1.next();
            Iterator var3 = listGui.list.iterator();

            while(var3.hasNext()) {
               String s = (String)var3.next();
               if (s.equalsIgnoreCase(listOfPacket.packet.getClass().getSimpleName())) {
                  list.add(listOfPacket);
                  break;
               }
            }
         }

         return list;
      }
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146124_l) {
         Iterator var2;
         AntiPacketPacket listOfPacket;
         switch(button.field_146127_k) {
         case 0:
            if (this.AllMobs.selected >= 0 && this.AllMobs.selected <= this.AllMobs.list.size() && !this.PacketToAdd.isEmpty() && !Packets.contains(this.PacketToAdd)) {
               Packets.add(this.PacketToAdd);
               AllPackets.remove(this.PacketToAdd);
               this.PacketToAdd = "";
            }
            break;
         case 1:
            Packets.remove(listGui.selected);
            AllPackets.add(this.PacketToRemove);
            break;
         case 2:
            this.field_146297_k.func_147108_a(new GuiYesNo(this, "Reset to Defaults", "Are you sure?", 0));
            break;
         case 3:
            this.field_146297_k.func_147108_a(Main.ClickGui);
            break;
         case 20:
            if (this.field_146297_k.field_71441_e != null) {
               var2 = ListOfPackets.iterator();

               while(var2.hasNext()) {
                  listOfPacket = (AntiPacketPacket)var2.next();
                  if (listOfPacket.packet.getClass().getSimpleName().startsWith("S") && !listGui.list.contains(listOfPacket.packet.getClass().getSimpleName())) {
                     listGui.list.add(listOfPacket.packet.getClass().getSimpleName());
                  }
               }
            }
            break;
         case 40:
            if (this.field_146297_k.field_71441_e != null) {
               var2 = ListOfPackets.iterator();

               while(var2.hasNext()) {
                  listOfPacket = (AntiPacketPacket)var2.next();
                  if (listOfPacket.packet.getClass().getSimpleName().startsWith("C") && !listGui.list.contains(listOfPacket.packet.getClass().getSimpleName())) {
                     listGui.list.add(listOfPacket.packet.getClass().getSimpleName());
                  }
               }
            }
         }

      }
   }

   public void func_73878_a(boolean result, int id) {
      if (id == 0 && result) {
         Packets.clear();
      }

      super.func_73878_a(result, id);
      this.field_146297_k.func_147108_a(this);
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      int mouseX = Mouse.getEventX() * this.field_146294_l / this.field_146297_k.field_71443_c;
      int mouseY = this.field_146295_m - Mouse.getEventY() * this.field_146295_m / this.field_146297_k.field_71440_d - 1;
      listGui.handleMouseInput(mouseX, mouseY);
      this.AllMobs.handleMouseInput(mouseX, mouseY);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.MobFieldName.func_146192_a(mouseX, mouseY, mouseButton);
      if (mouseX < 550 || mouseX > this.field_146294_l - 50 || mouseY < 32 || mouseY > this.field_146295_m - 64) {
         listGui.selected = -1;
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      this.MobFieldName.func_146201_a(typedChar, keyCode);
      if (keyCode == 28) {
         this.func_146284_a(this.addButton);
      } else if (keyCode == 211) {
         this.func_146284_a(this.removeButton);
      } else if (keyCode == 1) {
         this.func_146284_a(this.doneButton);
      }

      if (!this.MobFieldName.func_146179_b().isEmpty()) {
         this.AllMobs.list = PacketSearch(this.MobFieldName.func_146179_b());
      } else {
         this.AllMobs.list = removePackets(Packets);
      }

   }

   public static ArrayList<String> Getmobs() {
      return new ArrayList(listGui.list);
   }

   public void func_73876_c() {
      this.MobFieldName.func_146178_a();
      if (listGui.selected >= 0 && listGui.selected <= listGui.list.size()) {
         this.PacketToRemove = (String)listGui.list.get(listGui.selected);
      }

      if (this.AllMobs.selected >= 0 && this.AllMobs.selected < this.AllMobs.list.size()) {
         this.PacketToAdd = (String)this.AllMobs.list.get(this.AllMobs.selected);
      }

      this.addButton.field_146124_l = this.PacketToAdd != null;
      this.removeButton.field_146124_l = listGui.selected >= 0 && listGui.selected < listGui.list.size();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146297_k.field_71466_p, "Packet (" + listGui.getSize() + ")", this.field_146294_l / 2, 12, 16777215);
      listGui.drawScreen(mouseX, mouseY, partialTicks);
      this.AllMobs.drawScreen(mouseX, mouseY, partialTicks);
      this.MobFieldName.func_146194_f();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (this.MobFieldName.func_146179_b().isEmpty() && !this.MobFieldName.func_146206_l()) {
         this.func_73731_b(this.field_146297_k.field_71466_p, "Packet Name", 68, this.field_146295_m - 50, 8421504);
      }

      func_73734_a(48, this.field_146295_m - 56, 64, this.field_146295_m - 36, -6250336);
      func_73734_a(49, this.field_146295_m - 55, 64, this.field_146295_m - 37, -16777216);
      func_73734_a(214, this.field_146295_m - 56, 244, this.field_146295_m - 55, -6250336);
      func_73734_a(214, this.field_146295_m - 37, 244, this.field_146295_m - 36, -6250336);
      func_73734_a(244, this.field_146295_m - 56, 246, this.field_146295_m - 36, -6250336);
      func_73734_a(214, this.field_146295_m - 55, 243, this.field_146295_m - 52, -16777216);
      func_73734_a(214, this.field_146295_m - 40, 243, this.field_146295_m - 37, -16777216);
      func_73734_a(215, this.field_146295_m - 55, 216, this.field_146295_m - 37, -16777216);
      func_73734_a(242, this.field_146295_m - 55, 245, this.field_146295_m - 37, -16777216);
   }

   private void ListSetup() {
      ListOfPackets.clear();
      ListOfPackets.add(new AntiPacketPacket(new CPacketEncryptionResponse()));
      ListOfPackets.add(new AntiPacketPacket(new C00Handshake()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketLoginStart()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketDisconnect()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEnableCompression()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEncryptionRequest()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketLoginSuccess()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketAnimation()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketChatMessage()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketClickWindow()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketClientSettings()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketClientStatus()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketCloseWindow()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketConfirmTeleport()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketConfirmTransaction()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketCreativeInventoryAction()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketCustomPayload()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketEnchantItem()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketEntityAction()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketHeldItemChange()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketInput()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketKeepAlive()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketPlaceRecipe()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketPlayer()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketPlayerAbilities()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketPlayerDigging()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketPlayerTryUseItem()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketPing()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketPlayerTryUseItemOnBlock()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketRecipeInfo()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketResourcePackStatus()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketServerQuery()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketSeenAdvancements()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketSpectate()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketSteerBoat()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketTabComplete()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketUpdateSign()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketUseEntity()));
      ListOfPackets.add(new AntiPacketPacket(new CPacketVehicleMove()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketAdvancementInfo()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketAnimation()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketBlockAction()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketBlockBreakAnim()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketBlockChange()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketCamera()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketChangeGameState()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketChat()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketChunkData()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketCloseWindow()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketCollectItem()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketCombatEvent()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketConfirmTransaction()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketCooldown()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketCustomPayload()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketDestroyEntities()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketDisconnect()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketDisplayObjective()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEffect()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntity()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityAttach()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityEffect()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityEquipment()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityHeadLook()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityMetadata()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityProperties()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityStatus()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityTeleport()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketEntityVelocity()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketExplosion()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketHeldItemChange()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketJoinGame()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketKeepAlive()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketMaps()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketMoveVehicle()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketMultiBlockChange()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketOpenWindow()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketParticles()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketPlaceGhostRecipe()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketPlayerAbilities()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketPlayerListHeaderFooter()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketPlayerListItem()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketPlayerPosLook()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketRecipeBook()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketRemoveEntityEffect()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketResourcePackSend()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketRespawn()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketScoreboardObjective()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSelectAdvancementsTab()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketServerDifficulty()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSetExperience()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSetPassengers()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSetSlot()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSignEditorOpen()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSoundEffect()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSpawnExperienceOrb()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSpawnGlobalEntity()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSpawnMob()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSpawnObject()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSpawnPainting()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSpawnPlayer()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketSpawnPosition()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketStatistics()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketTabComplete()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketTeams()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketTimeUpdate()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketTitle()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketUnloadChunk()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketUpdateBossInfo()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketUpdateHealth()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketUpdateScore()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketUpdateTileEntity()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketUseBed()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketWindowItems()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketWindowProperty()));
      ListOfPackets.add(new AntiPacketPacket(new SPacketWorldBorder()));
   }

   private static class ListGui extends GuiScrollingList {
      private final Minecraft mc;
      private List<String> list;
      private int selected = -1;
      private int offsetx;

      public ListGui(Minecraft mc, AntiPacketGui screen, List<String> list, int offsetx, int offsety) {
         super(mc, screen.field_146294_l / 4, screen.field_146295_m, 32 + offsety, screen.field_146295_m - 64, 50 + offsetx, 16, screen.field_146294_l, screen.field_146295_m);
         this.offsetx = offsetx;
         this.mc = mc;
         this.list = list;
      }

      protected int getSize() {
         return this.list.size();
      }

      protected void elementClicked(int index, boolean doubleClick) {
         if (index >= 0 && index < this.list.size()) {
            this.selected = index;
         }

      }

      protected boolean isSelected(int index) {
         return index == this.selected;
      }

      protected void drawBackground() {
         Gui.func_73734_a(50 + this.offsetx, this.top, 66 + this.offsetx, this.bottom, -1);
      }

      protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
         FontRenderer fr = this.mc.field_71466_p;
         GlStateManager.func_179094_E();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179121_F();
         fr.func_78276_b(" (" + (String)this.list.get(slotIdx) + ")", 68 + this.offsetx, slotTop + 2, 15790320);
      }
   }
}
