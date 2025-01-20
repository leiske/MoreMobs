package moremobs.examples.mounts;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.stream.Stream;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.MaskShaderOptions;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.ai.behaviourTree.AINode;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.leaves.PlayerFollowerAINode;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.particle.FleshParticle;
import necesse.entity.particle.Particle;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;
import necesse.entity.mobs.summon.summonFollowingMob.mountFollowingMob.MountFollowingMob;


public class SpiderMountMob extends MountFollowingMob {
  private static final int[] frameOffsets = new int[] { 0, -8, -18, -30, -18, -8 };
  public static GameTexture texture;

  public SpiderMountMob() {
    super(50);

    setSpeed(50.0F);
    setFriction(2.0F);

    this.collision = new Rectangle(-10, -7, 20, 14);
    this.hitBox = new Rectangle(-12, -14, 24, 24);
    this.selectBox = new Rectangle(-16, -24, 32, 32);

    this.swimMaskMove = 8;
    this.swimMaskOffset = -8;
    this.swimSinkOffset = 0;
  }
  
  public void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
    super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);

    TextureDrawOptionsEnd front;

    int drawX = camera.getDrawX(x) - 48;
    int drawY = camera.getDrawY(y) - 64;
    int dir = getDir();

    GameLight light = level.getLightLevel(x / 32, y / 32);
    Point sprite = getAnimSprite(x, y, dir);

    drawY += getBobbing(x, y);
    drawY += getLevel().getTile(x / 32, y / 32).getMobSinkingAmount((Mob)this);

    final TextureDrawOptionsEnd behind = texture.initDraw().sprite(sprite.x, sprite.y, 64, 96).light(light).pos(drawX, drawY);

    front = texture.initDraw().sprite(sprite.x, sprite.y, 64, 96).light(light).pos(drawX, drawY);

    list.add(new MobDrawable() {
          public void draw(TickManager tickManager) {
            front.draw();
          }
          
          public void drawBehindRider(TickManager tickManager) {
            behind.draw();
          }
        });
  }
  
  
  public int getRiderMaskYOffset() {
    return -6;
  }
  
  public int getRiderDrawYOffset() {
    return 4;
  }

  public Point getSpriteOffset(int spriteX, int spriteY) {
    return new Point(getRiderDrawXOffset(), getRiderDrawYOffset());
  }
  
  public GameTexture getRiderMask() {
    return MobRegistry.Textures.mountmask;
  }
}

