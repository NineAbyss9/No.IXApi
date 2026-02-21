
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

public class ApiRemover {
    /* public static class EntityCallback<T extends EntityAccess> implements EntityInLevelCallback {
        private final T entity;
        private long currentSectionKey;
        private EntitySection<T> currentSection;
        private final LevelCallback<T> callback;

        public EntityCallback(ServerLevel level, T p_157614_, long p_157615_, EntitySection<T> p_157616_) {
            this.entity = p_157614_;
            this.currentSectionKey = p_157615_;
            this.currentSection = p_157616_;
            this.callback = level.callBacks;
        }

        public void onMove() {
        }

        public void onRemove(Entity.@NotNull RemovalReason p_157619_) {
            this.currentSection.remove(this.entity)
            Visibility visibility = PersistentEntitySectionManager.getEffectiveStatus(this.entity, this.currentSection.getStatus());
            if (visibility.isTicking()) {
                PersistentEntitySectionManager.this.stopTicking(this.entity);
            }
            if (visibility.isAccessible()) {
                PersistentEntitySectionManager.this.stopTracking(this.entity);
            }
            if (p_157619_.shouldDestroy()) {
                PersistentEntitySectionManager.this.callbacks.onDestroyed(this.entity);
            }
            PersistentEntitySectionManager.this.knownUuids.remove(this.entity.getUUID());
            PersistentEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
        }
    }*/
}
