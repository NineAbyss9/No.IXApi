
package com.bilibili.player_ix.noixmod_api.client.model.animations;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class YetiAnims {
	public static final AnimationDefinition pose = AnimationDefinition.Builder.withLength(0.0F)
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.0F, 4.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.9615F, -9.9904F, -0.4407F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone9", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.3965F, -3.2445F, 6.2811F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-19.8265F, 2.0007F, 1.4112F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.9615F, 9.9904F, 0.4407F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.4771F, -1.9658F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.3965F, 3.2445F, -6.2811F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-19.8265F, 2.0007F, 1.4112F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(2.0F).looping()
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.4589F, 0.6553F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.9876F, -0.3256F, -2.4831F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(1.7418F, 0.413F, 2.7772F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.3761F, 0.5904F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(7.84F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-3.72F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.9876F, 0.3256F, 2.4831F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(1.7418F, -0.413F, -2.7772F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(1.0F)
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-3.5915F, -37.2851F, -2.7076F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-3.5915F, -37.2851F, -2.7076F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-10.2386F, -42.1791F, -4.0406F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(16.3225F, 22.6769F, 0.6361F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(56.0723F, 2.6391F, -7.1023F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(56.0723F, 2.6391F, -7.1023F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(58.5723F, 2.6391F, -7.1023F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(14.413F, 6.3627F, -3.176F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-4.4968F, 8.9656F, 0.6669F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 2.87F, -3.38F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -2.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(14.53F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-53.929F, -22.8514F, 12.7657F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-127.5F, 0.0F, 52.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-127.5F, 0.0F, 52.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-132.5F, 0.0F, 52.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-72.1747F, 7.2478F, 57.7113F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 52.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(1.621F, -7.5731F, -5.3871F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(1.621F, -7.5731F, -5.3871F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(1.6244F, -2.3988F, 1.9078F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.scaleVec(1.0F, 1.4F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-23.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(68.2138F, -12.4477F, 0.8583F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -3.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 37.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(0.0F, 37.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(13.392F, -9.2976F, -13.4331F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-0.053F, -9.8358F, -18.1923F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-0.053F, -9.8358F, -18.1923F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(7.447F, -9.8358F, -18.1923F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-51.1324F, 25.1467F, -49.7126F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-28.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition attack2 = AnimationDefinition.Builder.withLength(1.0F)
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-2.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 1.9696F, 0.3473F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 3.9696F, 0.3473F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 1.9696F, 0.3473F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -0.2857F, -4.8714F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(5.1703F, 9.1602F, 0.006F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(5.1703F, 9.1602F, 0.006F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(50.5176F, 9.9609F, 0.0102F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(62.5F, 10.0F, 0.01F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0191F, -2.6831F, -6.4654F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.01F, 0.66F, -3.23F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-29.06F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-199.9994F, 6.4715F, -3.7928F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-200.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-200.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-36.7628F, 5.501F, 5.1001F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0817F, -1.4491F, -4.23F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-48.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-43.39F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-31.86F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(18.67F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(52.4214F, -8.6511F, 2.5179F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(52.4214F, -8.6511F, 2.5179F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-16.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-28.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.5032F, 0.9002F, 2.8172F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.5032F, 0.9002F, 2.8172F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(-0.1362F, 5.4822F, 1.5751F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(-0.0065F, -5.1374F, -4.9002F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -4.21F, -4.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.7F, -3.49F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-26.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-24.97F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-24.97F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(19.38F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(30.1F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-190.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-190.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-188.54F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-92.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-40.1783F, -7.7363F, -6.3416F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(-0.1288F, -1.0774F, -4.3385F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-36.6457F, 3.1006F, -1.4457F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition attack3 = AnimationDefinition.Builder.withLength(1.0F)
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-2.8299F, -24.511F, -10.6229F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-2.8299F, -24.511F, -10.6229F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-7.8299F, -24.511F, -10.6229F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-3.012F, -6.5509F, 1.1612F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(37.7164F, 10.1415F, 11.5069F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(37.7164F, 10.1415F, 11.5069F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(31.7918F, 11.8197F, 13.1838F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(19.8222F, 19.0264F, 17.7574F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(53.0702F, 11.0413F, -1.6915F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(53.07F, 11.04F, -1.69F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(3.07F, 11.0399F, -1.6899F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-16.93F, 11.04F, -1.69F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-16.93F, 11.04F, -1.69F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 4.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 5.25F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, -4.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -4.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0.3813F, -12.2889F, 19.8234F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-0.3813F, -12.2889F, 19.8234F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-16.1031F, -22.1017F, 21.8825F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(46.9232F, 22.613F, 31.0283F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(46.9232F, 22.613F, 31.0283F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-8.24F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-7.5F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-7.5F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(60.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(60.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 1.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(12.5F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(12.5F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(5.0F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(10.2352F, 7.6185F, 1.8155F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-12.0819F, -6.4561F, 3.2276F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-12.0819F, -6.4561F, 3.2276F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-9.092F, -13.8635F, 5.9466F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(60.0F, 0.0F, -115.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(60.0F, 0.0F, -115.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(75.5696F, -9.6505F, -135.3573F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-3.5735F, -16.2529F, -142.665F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-77.9863F, 2.4255F, -169.8601F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-67.1853F, 12.7252F, -131.2296F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-44.8235F, 15.6405F, -54.8805F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-33.8653F, 27.088F, -32.3846F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.posVec(1.3992F, 0.3775F, 0.1438F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(1.618F, -2.808F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(1.618F, -2.808F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.4583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.0F, 1.3F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-21.36F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition snowball = AnimationDefinition.Builder.withLength(1.4583F)
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(34.2465F, 0.1222F, 3.2833F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(39.2465F, 0.1222F, 3.2833F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-33.2424F, 0.6608F, -0.909F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-43.2424F, 0.6608F, -0.909F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-38.2424F, -0.6608F, 0.909F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-21.3674F, -0.7434F, 1.0226F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(11.7576F, -0.6608F, 0.909F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(16.7576F, -0.6608F, 0.909F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 3.9962F, -0.1745F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 3.9962F, -0.1745F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.622F, -3.8564F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.62F, -3.06F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 1.62F, -3.06F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 1.62F, -3.06F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0326F, 4.8474F, -5.5682F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, -0.62F, -6.49F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, -0.62F, -6.49F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 1.44F, -3.23F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.3976F, -1.5295F, -0.9609F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.3976F, -1.5295F, -0.9609F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-17.1024F, -1.5295F, -0.9609F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-19.6024F, -1.5295F, -0.9609F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(0.3976F, 1.5295F, 0.9609F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.3976F, 1.5295F, 0.9609F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(48.5277F, 13.285F, -3.7523F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(48.5277F, 13.285F, -3.7523F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(41.877F, 10.9209F, 1.8375F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, -5.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -4.54F, -3.93F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, -1.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -1.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.41F, -6.35F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, -1.86F, -7.07F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, -1.86F, -7.07F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.posVec(0.0F, 2.72F, -1.07F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-9.11F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-9.11F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-9.11F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(9.58F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(0.0479F, 1.2215F, -1.5829F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(-0.2018F, 0.1349F, 0.3693F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(-0.2018F, 0.1349F, 0.3693F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.posVec(-0.2018F, 0.1349F, 0.3693F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(20.5357F, -29.4403F, 33.6542F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(40.5357F, -29.4403F, 33.6542F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-49.1407F, -13.4487F, 12.3309F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-38.244F, -8.0369F, 5.7891F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-196.4087F, 17.0144F, -11.9274F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-206.4087F, 17.0144F, -11.9274F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-199.7053F, 19.9251F, -15.8515F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-151.1981F, 7.548F, 0.8154F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-103.3201F, 2.4586F, -5.9734F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-90.8201F, 2.4586F, -5.9734F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-34.5685F, 3.0801F, 5.9223F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(-0.0328F, -2.6536F, -4.2376F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(-0.03F, -2.44F, -3.9F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(-0.03F, -2.44F, -3.9F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.619F, 0.3616F, -0.0008F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.61F, 0.43F, 0.11F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.6425F, -3.4748F, -5.9649F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.posVec(-0.3785F, -3.5544F, -5.8835F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.8333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9167F, KeyframeAnimations.scaleVec(1.0F, 1.3F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-52.97F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-10.73F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(60.9973F, -3.2371F, 6.8394F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(60.9973F, -3.2371F, 6.8394F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(45.9973F, -3.2371F, 6.8394F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(55.9973F, -3.2371F, 6.8394F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(68.0743F, -4.7623F, 6.028F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(68.0743F, -4.7623F, 6.028F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-12.1648F, -9.2944F, 0.6669F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-17.2868F, -10.4048F, -0.6757F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-17.2868F, -10.4048F, -0.6757F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-0.2847F, 0.2324F, -1.6262F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(-0.2847F, 0.2324F, -1.6262F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(-0.28F, -0.77F, -6.63F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(-0.28F, -2.77F, -6.63F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(0.28F, -1.77F, -4.63F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.28F, -1.77F, -4.63F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.33F, 3.6F, -7.15F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.33F, -3.84F, -7.85F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.posVec(0.33F, -3.84F, -7.85F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(51.37F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(15.98F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(15.98F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(57.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(4.1F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.125F, KeyframeAnimations.degreeVec(11.44F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(16.3969F, 22.1017F, -36.8825F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(31.3969F, 22.1017F, -36.8825F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-49.7918F, 9.1238F, -14.9236F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-42.9744F, -1.1471F, -0.6203F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-198.1959F, -20.5619F, 18.9576F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-205.6959F, -20.5619F, 18.9576F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-198.0207F, -12.9646F, 9.8991F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-150.5668F, -3.7364F, -3.092F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-102.5845F, -1.9384F, 3.9855F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-92.5845F, -1.9384F, 3.9855F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-35.1787F, -2.4577F, -6.4376F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(0.0328F, -2.6536F, -4.2376F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(0.03F, -2.44F, -3.9F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.03F, -2.44F, -3.9F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(-0.4252F, 0.3602F, 0.03F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(-0.42F, 0.43F, 0.14F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(-0.3785F, -3.5544F, -5.8835F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.posVec(-0.3785F, -3.5544F, -5.8835F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.8333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9167F, KeyframeAnimations.scaleVec(1.0F, 1.3F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-54.38F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-12.58F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition move = AnimationDefinition.Builder.withLength(1.25F).looping()
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0175F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(5.0175F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(5.0175F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3125F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9375F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0938F, KeyframeAnimations.degreeVec(2.5F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.posVec(0.02F, 2.02F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4688F, KeyframeAnimations.posVec(0.03F, 0.84F, 0.21F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.03F, -1.966F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.posVec(0.02F, 3.02F, 0.13F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0938F, KeyframeAnimations.degreeVec(21.72F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.degreeVec(-7.652F, -4.7563F, 1.5498F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.degreeVec(3.1121F, 7.4397F, -0.9498F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.degreeVec(-8.59F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.degreeVec(-0.87F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4688F, KeyframeAnimations.degreeVec(2.5F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.03F, -1.966F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.posVec(0.02F, 3.02F, 0.13F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.posVec(0.01F, 0.54F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0938F, KeyframeAnimations.posVec(0.02F, -0.64F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.posVec(0.03F, -1.966F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4688F, KeyframeAnimations.degreeVec(20.47F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.degreeVec(2.4915F, -7.429F, 1.0302F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.degreeVec(-8.2869F, 4.739F, -1.6024F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.degreeVec(-8.59F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9375F, KeyframeAnimations.degreeVec(-0.87F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition moveToStride = AnimationDefinition.Builder.withLength(0.1667F)
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0175F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-7.4825F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-2.5F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, 1.5F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-3.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-5.3816F, -9.634F, 2.7107F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(54.8863F, -7.8893F, 0.4753F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.03F, -1.966F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.03F, 0.034F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-7.3711F, 9.8607F, -10.8188F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition stride = AnimationDefinition.Builder.withLength(0.8503F).looping()
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.4825F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(-7.4825F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(-7.4825F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2126F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4337F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.6378F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.8503F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(46.2628F, 8.5119F, -2.8097F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7483F, KeyframeAnimations.degreeVec(2.5F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(-2.5F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.posVec(0.02F, 2.02F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3316F, KeyframeAnimations.posVec(0.03F, 0.84F, 0.21F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.posVec(-0.0172F, -1.0074F, -0.0244F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.posVec(0.02F, 3.02F, 0.13F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.posVec(0.0F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7483F, KeyframeAnimations.degreeVec(21.72F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.3816F, -9.634F, 2.7107F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.degreeVec(-7.652F, -4.7563F, 1.5498F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.degreeVec(3.1121F, 7.4397F, -0.9498F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(-5.3816F, -9.634F, 2.7107F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.degreeVec(-8.59F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.degreeVec(-0.87F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(54.8863F, -7.8893F, 0.4753F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3316F, KeyframeAnimations.degreeVec(2.5F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(-2.5F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(54.8863F, -7.8893F, 0.4753F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.03F, 0.034F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.posVec(0.02F, 3.02F, 0.13F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.posVec(0.0F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.posVec(0.01F, 0.54F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7483F, KeyframeAnimations.posVec(0.02F, -0.64F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.posVec(0.03F, 0.034F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3316F, KeyframeAnimations.degreeVec(20.47F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.3711F, 9.8607F, -10.8188F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.degreeVec(2.4915F, -7.429F, 1.0302F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.degreeVec(-8.2869F, 4.739F, -1.6024F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(-7.3711F, 9.8607F, -10.8188F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2126F, KeyframeAnimations.degreeVec(-8.59F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4337F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6378F, KeyframeAnimations.degreeVec(-0.87F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8503F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition strideToRun = AnimationDefinition.Builder.withLength(0.1667F)
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.4825F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(61.9825F, 12.3051F, -2.0728F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(-0.0953F, 0.9823F, -3.1361F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -5.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.3816F, -9.634F, 2.7107F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-7.6739F, 5.3777F, -4.5864F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.04F, -0.76F, -2.12F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.038F, -0.7555F, -2.1244F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(54.8863F, -7.8893F, 0.4753F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(24.9431F, -10.1947F, 0.2377F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-5.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.03F, 0.034F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(-0.325F, 2.3369F, -2.7582F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-0.7469F, -1.3614F, -6.0363F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 3.6F, 0.23F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 3.5981F, 0.2321F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.3711F, 9.8607F, -10.8188F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-79.7633F, -5.9436F, 3.8241F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.06F, -0.43F, -1.95F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-0.0641F, -1.0629F, -5.0505F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition run = AnimationDefinition.Builder.withLength(0.5F).looping()
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0625F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(51.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(51.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(61.9825F, 12.3051F, -2.0728F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(75.6445F, 12.3309F, -1.7993F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-5.0F, 12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(61.9825F, 12.3051F, -2.0728F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -5.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1875F, KeyframeAnimations.posVec(-0.65F, 2.16F, -5.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(-0.7469F, -1.3614F, -6.0363F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -5.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(34.83F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(40.53F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1875F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.6739F, 5.3777F, -4.5864F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-78.7186F, 5.6606F, -4.2052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-79.7633F, 5.9436F, -3.8241F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-7.6739F, 5.3777F, -4.5864F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.038F, -0.7555F, -2.1244F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0625F, KeyframeAnimations.posVec(0.01F, 3.56F, 1.2481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(-0.01F, 4.8741F, -0.5759F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(-0.0641F, -1.0629F, -5.0505F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(-0.1211F, -1.6001F, -2.5582F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.038F, -0.7555F, -2.1244F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-38.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(61.9825F, -12.3051F, 2.0728F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(77.7961F, -12.3173F, 1.9432F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-5.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.7469F, -1.3614F, -6.0363F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -5.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4375F, KeyframeAnimations.posVec(-0.65F, 2.16F, -5.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(-0.7469F, -1.3614F, -6.0363F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(27.72F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(40.53F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4375F, KeyframeAnimations.degreeVec(31.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-33.8F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-33.8F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 3.5981F, 0.2321F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 3.5981F, 0.2321F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-79.7633F, -5.9436F, 3.8241F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-7.6739F, -5.3777F, 4.5864F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-66.2186F, -5.6606F, 4.2052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-79.7633F, -5.9436F, 3.8241F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.0641F, -1.0629F, -5.0505F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.1011F, -1.5579F, -2.5929F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.038F, -0.7555F, -2.1244F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3125F, KeyframeAnimations.posVec(0.01F, 3.56F, 1.2481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(0.01F, 4.8741F, -0.5759F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(-0.0641F, -1.0629F, -5.0505F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-38.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition runToMove = AnimationDefinition.Builder.withLength(0.125F)
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(5.0175F, 2.3491F, 0.8555F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(61.9825F, 12.3051F, -2.0728F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -5.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, 1.48F, -1.41F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(16.3F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.6739F, 5.3777F, -4.5864F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.038F, -0.7555F, -2.1244F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.7469F, -1.3614F, -6.0363F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.03F, -1.966F, 0.2562F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 3.5981F, 0.2321F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-79.7633F, -5.9436F, 3.8241F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.0641F, -1.0629F, -5.0505F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition hide = AnimationDefinition.Builder.withLength(1.0F)
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.6383F, -1.1472F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone9", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-65.7058F, 5.549F, -7.0728F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.3132F, -1.8943F, -4.8392F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 1.91F, -2.82F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-65.7058F, -5.549F, 7.0728F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(-0.2365F, -2.0681F, -3.8287F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition hidePose = AnimationDefinition.Builder.withLength(0.0F)
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.6383F, -1.1472F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone9", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-65.7058F, 5.549F, -7.0728F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.3132F, -1.8943F, -4.8392F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone12", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.91F, -2.82F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-65.7058F, -5.549F, 7.0728F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.2365F, -2.0681F, -3.8287F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bone14", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();
}