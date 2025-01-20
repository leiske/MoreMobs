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

public class GrizzlyBearMountMob extends MountFollowingMob {
 public static GameTexture texture;

  private int width = 128;
  private int height = 128;

  public GrizzlyBearMountMob() {
    super(50);

    setSpeed(100.0F);
    setSwimSpeed(0.9F);
    setFriction(5.0F);
    this.collision = new Rectangle(-10, -7, 20, 14);
    this.hitBox = new Rectangle(-12, -14, 24, 24);
    this.selectBox = new Rectangle(-16, -24, 32, 32);

    this.swimMaskMove = 8;
    this.swimMaskOffset = -8;
    this.swimSinkOffset = 0;
  }

  public void init() {
    super.init();
    this.ai = new BehaviourTreeAI((Mob)this, (AINode)new PlayerFollowerAINode(480, 128));
  }
  
  public void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
    super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);

    TextureDrawOptionsEnd front;

    int drawX = camera.getDrawX(x) - 64;
    int drawY = camera.getDrawY(y) - 64;
    int dir = getDir();

    GameLight light = level.getLightLevel(x / 32, y / 32);
    Point sprite = getAnimSprite(x, y, dir);

    drawY += getBobbing(x, y);
    drawY += getLevel().getTile(x / 32, y / 32).getMobSinkingAmount((Mob)this);
    final MaskShaderOptions swimMask = getSwimMaskShaderOptions(inLiquidFloat(x, y));
    final TextureDrawOptionsEnd options = texture.initDraw().sprite(sprite.x, sprite.y, 124, 124).addMaskShader(swimMask).light(light).pos(drawX, drawY);
    final TextureDrawOptionsEnd behind = texture.initDraw().sprite(sprite.x, sprite.y, width, height).light(light).pos(drawX, drawY);

    front = texture.initDraw().sprite(sprite.x, sprite.y, width, height).light(light).pos(drawX, drawY);

    list.add(new MobDrawable() {
          public void draw(TickManager tickManager) {
            // behind.draw();
          }
          
          public void drawBehindRider(TickManager tickManager) {
            front.draw();
          }
        });
  }

  public Point getSpriteOffset(int spriteX, int spriteY) {
    Point p = new Point(0, 0);
    if (spriteX == 1 || spriteX == 2)
      p.y = 2; 
    p.x += getRiderDrawXOffset();
    p.y += getRiderDrawYOffset();
    return p;
  }
  
  public int getRiderDrawYOffset() {
    return -4;
  }  
  
  public int getRiderArmSpriteX() {
    return 0;
  }
  
  public int getRiderSpriteX() {
    return 0;
  }

  public MaskShaderOptions getRiderMaskOptions(int x, int y) {
    GameTexture riderMask = getRiderMask();
    Point spriteOffset = getSpriteOffset(getAnimSprite(x, y, getDir()));
    if (riderMask != null) {
      int maskXOffset = getRiderMaskXOffset();
      int maskYOffset = getRiderMaskYOffset();

      int drawXOffset = getRiderDrawXOffset();
      int drawYOffset = getRiderDrawYOffset();

      // 0 - up facing
      // 1 - right facing
      // 2 - down facing
      // 3 - left facing
      int dir = getDir();

      if (dir == 2)
        drawYOffset += -8;

      System.out.println("Rider Mask: " + " x: " + maskXOffset + " y: " + maskYOffset + " Dir: " + dir);

      return new MaskShaderOptions(
        riderMask,
        spriteOffset.x + drawXOffset,
        spriteOffset.y + drawYOffset,
        maskXOffset,
        maskYOffset
      );
    }

    return new MaskShaderOptions(spriteOffset.x, spriteOffset.y);
  }
  
  public GameTexture getRiderMask() {
    return MobRegistry.Textures.mountmask;
  }

   protected void doMountedLogic() {
    if (isServer())
      return; 
    int particleCount = 40;
    for (int i = 0; i < particleCount; i++)
      getLevel().entityManager.addParticle(this.x + 
          (float)(GameRandom.globalRandom.nextGaussian() * 8.0D), this.y + 16.0F + 
          (float)(GameRandom.globalRandom.nextGaussian() * 8.0D), Particle.GType.IMPORTANT_COSMETIC)
        
        .sprite(GameResources.mapleLeafParticles.sprite(0, 0, 32))
        .lifeTime(750)
        .fadesAlphaTime(100, 250)
        .movesFriction(16.0F * (float)GameRandom.globalRandom.nextGaussian(), 5.0F * 
          (float)GameRandom.globalRandom.nextGaussian(), 1.0F)
        .sizeFades(14, 18)
        .heightMoves(20.0F, 64.0F); 
  }

  @Override
  public int getRockSpeed() {
      // Change the speed at which this mobs animation plays
      return 50;
  }
}
