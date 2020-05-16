package onimen.anni.hmage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import onimen.anni.hmage.Preferences;

public class CustomMovementInput extends MovementInput {

  private final GameSettings gameSettings;
  private boolean toggleSneaking;
  private long lastPressed;

  public CustomMovementInput(GameSettings gameSettings) {
    this.gameSettings = gameSettings;
    this.toggleSneaking = false;
    this.lastPressed = 0;
  }

  @Override
  public void updatePlayerMoveState() {

    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayerSP player = mc.player;

    this.moveStrafe = 0F;
    this.moveForward = 0F;

    if (this.gameSettings.keyBindForward.isKeyDown()) {
      ++this.moveForward;
      this.forwardKeyDown = true;
    } else {
      this.forwardKeyDown = false;
    }

    if (this.gameSettings.keyBindBack.isKeyDown()) {
      --this.moveForward;
      this.backKeyDown = true;
    } else {
      this.backKeyDown = false;
    }

    if (this.gameSettings.keyBindLeft.isKeyDown()) {
      ++this.moveStrafe;
      this.leftKeyDown = true;
    } else {
      this.leftKeyDown = false;
    }

    if (this.gameSettings.keyBindRight.isKeyDown()) {
      --this.moveStrafe;
      this.rightKeyDown = true;
    } else {
      this.rightKeyDown = false;
    }

    this.jump = this.gameSettings.keyBindJump.isKeyDown();

    KeyBinding keyBindSneak = this.gameSettings.keyBindSneak;
    KeyBinding keyBindSprint = this.gameSettings.keyBindSprint;

    if (Preferences.toggleSneak) {

      if (keyBindSneak.isKeyDown()) {

        if (keyBindSneak.isPressed()) {
          if (!this.toggleSneaking && !player.isRiding() && !player.capabilities.isFlying) {
            this.lastPressed = System.currentTimeMillis();
          }
          this.toggleSneaking = !this.toggleSneaking;
        } else {
          this.sneak = true;
          if (System.currentTimeMillis() - this.lastPressed > Preferences.toggleSneakThreshold) {
            this.toggleSneaking = false;
          }
        }

      } else {
        this.sneak = this.toggleSneaking;
      }

    } else {
      this.sneak = keyBindSneak.isKeyDown();
    }

    if (this.sneak) {
      this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
      this.moveForward = (float) ((double) this.moveForward * 0.3D);
    }

    if (Preferences.toggleSprint) {
      if (keyBindSprint.isKeyDown()) {

        if (keyBindSprint.isPressed()) {
        }

      }
    }
  }
}
