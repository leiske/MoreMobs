package moremobs.examples.mounts;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.stream.Stream;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameUtils;
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
  public static GameTexture texture;

  private int width = 96;
  private int height = 96;

  private float defaultSpeed = 100.0F;

  public SpiderMountMob() {
    super(50);

    setSpeed(defaultSpeed);
    setSwimSpeed(0.9F);
    setFriction(5.0F);

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
    int drawY = camera.getDrawY(y) - 48;
    int dir = getDir();

    GameLight light = level.getLightLevel(x / 32, y / 32);
    Point sprite = getAnimSprite(x, y, dir);

    int animationTime = 1000;

    long time = level.getTime() + (new GameRandom(getUniqueID())).nextInt(animationTime);

    float bobbingFloat = GameUtils.getBobbing(time, animationTime);
    
    drawY += getLevel().getTile(x / 32, y / 32).getMobSinkingAmount((Mob)this);
    drawY += (int)(bobbingFloat * 2);

    front = texture.initDraw().sprite(sprite.x, sprite.y, width, height).light(light).pos(drawX, drawY);

    list.add(new MobDrawable() {
        public void draw(TickManager tickManager) { }
        
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

  
  public int getRiderMaskYOffset() {
    return -4;
  }
  
  public int getRiderDrawYOffset() {
    return -4;
  }

  public void serverTick() {
    super.serverTick();
    System.out.println("server tick");
  }

  public MaskShaderOptions getRiderMaskOptions(int x, int y) {
    GameTexture riderMask = getRiderMask();
    Point spriteOffset = getSpriteOffset(getAnimSprite(x, y, getDir()));

    if (riderMask == null) {
      return new MaskShaderOptions(spriteOffset.x, spriteOffset.y);
    }

    int maskXOffset = getRiderMaskXOffset();
    int maskYOffset = getRiderMaskYOffset();

    int drawXOffset = getRiderDrawXOffset();
    int drawYOffset = getRiderDrawYOffset();

    // 0 - up facing
    // 1 - right facing
    // 2 - down facing
    // 3 - left facing
    int dir = getDir();

    if (dir == 1) // right
      drawXOffset += 10;
    if (dir == 3) // left
      drawXOffset += -10;

    if (dir == 0) // up
      maskYOffset += 4;

    System.out.println("Rider Mask: " + " x: " + maskXOffset + " y: " + maskYOffset + " Dir: " + dir);

    return new MaskShaderOptions(
      riderMask,
      spriteOffset.x + drawXOffset,
      spriteOffset.y + drawYOffset,
      maskXOffset,
      maskYOffset
    );
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

