
package com.bilibili.player_ix.noixmod_api.network;

import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;

public class ApiServerPacketListener implements ServerPlayerConnection, TickablePacketListener, ServerGamePacketListener {
    public void tick() {

    }

    public void handleAnimate(ServerboundSwingPacket pPacket) {

    }

    public void handleChat(ServerboundChatPacket pPacket) {

    }

    public void handleChatCommand(ServerboundChatCommandPacket pPacket) {

    }

    public void handleChatAck(ServerboundChatAckPacket pPacket) {

    }

    public void handleClientCommand(ServerboundClientCommandPacket pPacket) {

    }

    public void handleClientInformation(ServerboundClientInformationPacket pPacket) {

    }

    public void handleContainerButtonClick(ServerboundContainerButtonClickPacket pPacket) {

    }

    public void handleContainerClick(ServerboundContainerClickPacket pPacket) {

    }

    public void handlePlaceRecipe(ServerboundPlaceRecipePacket pPacket) {

    }

    public void handleContainerClose(ServerboundContainerClosePacket pPacket) {

    }

    public void handleCustomPayload(ServerboundCustomPayloadPacket pPacket) {

    }

    public void handleInteract(ServerboundInteractPacket pPacket) {

    }

    public void handleKeepAlive(ServerboundKeepAlivePacket pPacket) {

    }

    public void handleMovePlayer(ServerboundMovePlayerPacket pPacket) {

    }

    public void handlePong(ServerboundPongPacket pPacket) {

    }

    public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket pPacket) {

    }

    public void handlePlayerAction(ServerboundPlayerActionPacket pPacket) {

    }

    public void handlePlayerCommand(ServerboundPlayerCommandPacket pPacket) {

    }

    public void handlePlayerInput(ServerboundPlayerInputPacket pPacket) {

    }

    public void handleSetCarriedItem(ServerboundSetCarriedItemPacket pPacket) {

    }

    public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket pPacket) {

    }

    public void handleSignUpdate(ServerboundSignUpdatePacket pPacket) {

    }

    public void handleUseItemOn(ServerboundUseItemOnPacket pPacket) {

    }

    public void handleUseItem(ServerboundUseItemPacket pPacket) {

    }

    public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket pPacket) {

    }

    public void handleResourcePackResponse(ServerboundResourcePackPacket pPacket) {

    }

    public void handlePaddleBoat(ServerboundPaddleBoatPacket pPacket) {

    }

    public void handleMoveVehicle(ServerboundMoveVehiclePacket pPacket) {

    }

    public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket pPacket) {

    }

    public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket pPacket) {

    }

    public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket pPacket) {

    }

    public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket pPacket) {

    }

    public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket pPacket) {

    }

    public void handleSetCommandBlock(ServerboundSetCommandBlockPacket pPacket) {

    }

    public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket pPacket) {

    }

    public void handlePickItem(ServerboundPickItemPacket pPacket) {

    }

    public void handleRenameItem(ServerboundRenameItemPacket pPacket) {

    }

    public void handleSetBeaconPacket(ServerboundSetBeaconPacket pPacket) {

    }

    public void handleSetStructureBlock(ServerboundSetStructureBlockPacket pPacket) {

    }

    public void handleSelectTrade(ServerboundSelectTradePacket pPacket) {

    }

    public void handleEditBook(ServerboundEditBookPacket pPacket) {

    }

    public void handleEntityTagQuery(ServerboundEntityTagQuery pPacket) {

    }

    public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQuery pPacket) {

    }

    public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket pPacket) {

    }

    public void handleJigsawGenerate(ServerboundJigsawGeneratePacket pPacket) {

    }

    public void handleChangeDifficulty(ServerboundChangeDifficultyPacket pPacket) {

    }

    public void handleLockDifficulty(ServerboundLockDifficultyPacket pPacket) {

    }

    public void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket pPacket) {

    }

    public void onDisconnect(Component pReason) {

    }

    public boolean isAcceptingMessages() {
        return false;
    }

    public ServerPlayer getPlayer() {
        return null;
    }

    public void send(Packet<?> pPacket) {

    }
}
