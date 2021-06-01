package Method.Client.module.combat;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

class Hole extends Vec3i {
   private final boolean tall;
   private Hole.HoleTypes HoleType;

   public Hole(int x, int y, int z, BlockPos pos, Hole.HoleTypes p_Type, boolean tall) {
      super(x, y, z);
      this.tall = tall;
      this.SetHoleType(p_Type);
   }

   public boolean isTall() {
      return this.tall;
   }

   public Hole.HoleTypes GetHoleType() {
      return this.HoleType;
   }

   public void SetHoleType(Hole.HoleTypes holeType) {
      this.HoleType = holeType;
   }

   public static enum HoleTypes {
      None,
      Obsidian,
      Bedrock,
      Void,
      Burrow;
   }
}
