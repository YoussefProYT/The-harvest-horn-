#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

move_if_exists() {
  local src="$1"
  local dst="$2"
  local src_path="$SCRIPT_DIR/$src"
  local dst_path="$SCRIPT_DIR/$dst"

  if [ -e "$src_path" ]; then
    mkdir -p "$(dirname "$dst_path")"
    mv "$src_path" "$dst_path"
    echo "Moved $src -> $dst"
  fi
}

mkdir -p "$SCRIPT_DIR/.devcontainer" \
  "$SCRIPT_DIR/gradle/wrapper" \
  "$SCRIPT_DIR/src/main/java/com/yousef/harvesthorn/item" \
  "$SCRIPT_DIR/src/main/resources/assets/harvesthorn/lang" \
  "$SCRIPT_DIR/src/main/resources/assets/harvesthorn/models/item" \
  "$SCRIPT_DIR/src/main/resources/data/harvesthorn/recipe"

move_if_exists "devcontainer.json" ".devcontainer/devcontainer.json"
move_if_exists "HarvestHorn.java" "src/main/java/com/yousef/harvesthorn/HarvestHorn.java"
move_if_exists "HarvestHornItem.java" "src/main/java/com/yousef/harvesthorn/item/HarvestHornItem.java"
move_if_exists "ModItems.java" "src/main/java/com/yousef/harvesthorn/item/ModItems.java"
move_if_exists "en_us.json" "src/main/resources/assets/harvesthorn/lang/en_us.json"
move_if_exists "diamond_harvest_horn.json" "src/main/resources/assets/harvesthorn/models/item/diamond_harvest_horn.json"
move_if_exists "gold_harvest_horn.json" "src/main/resources/assets/harvesthorn/models/item/gold_harvest_horn.json"
move_if_exists "iron_harvest_horn.json" "src/main/resources/assets/harvesthorn/models/item/iron_harvest_horn.json"
move_if_exists "netherite_harvest_horn.json" "src/main/resources/assets/harvesthorn/models/item/netherite_harvest_horn.json"

printf '\nOrganization complete.\n'
