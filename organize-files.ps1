$ErrorActionPreference = 'Stop'
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

function Move-IfExists {
    param(
        [string]$Source,
        [string]$Destination
    )

    $sourcePath = Join-Path $scriptDir $Source
    $destinationPath = Join-Path $scriptDir $Destination

    if (Test-Path $sourcePath) {
        New-Item -ItemType Directory -Path (Split-Path -Parent $destinationPath) -Force | Out-Null
        Move-Item -Path $sourcePath -Destination $destinationPath -Force
        Write-Host "Moved $Source -> $Destination"
    }
}

New-Item -ItemType Directory -Path (Join-Path $scriptDir '.devcontainer') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $scriptDir 'gradle/wrapper') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $scriptDir 'src/main/java/com/yousef/harvesthorn/item') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $scriptDir 'src/main/resources/assets/harvesthorn/lang') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $scriptDir 'src/main/resources/assets/harvesthorn/models/item') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $scriptDir 'src/main/resources/data/harvesthorn/recipe') -Force | Out-Null

Move-IfExists -Source 'devcontainer.json' -Destination '.devcontainer/devcontainer.json'
Move-IfExists -Source 'HarvestHorn.java' -Destination 'src/main/java/com/yousef/harvesthorn/HarvestHorn.java'
Move-IfExists -Source 'HarvestHornItem.java' -Destination 'src/main/java/com/yousef/harvesthorn/item/HarvestHornItem.java'
Move-IfExists -Source 'ModItems.java' -Destination 'src/main/java/com/yousef/harvesthorn/item/ModItems.java'
Move-IfExists -Source 'en_us.json' -Destination 'src/main/resources/assets/harvesthorn/lang/en_us.json'
Move-IfExists -Source 'diamond_harvest_horn.json' -Destination 'src/main/resources/assets/harvesthorn/models/item/diamond_harvest_horn.json'
Move-IfExists -Source 'gold_harvest_horn.json' -Destination 'src/main/resources/assets/harvesthorn/models/item/gold_harvest_horn.json'
Move-IfExists -Source 'iron_harvest_horn.json' -Destination 'src/main/resources/assets/harvesthorn/models/item/iron_harvest_horn.json'
Move-IfExists -Source 'netherite_harvest_horn.json' -Destination 'src/main/resources/assets/harvesthorn/models/item/netherite_harvest_horn.json'

Write-Host "`nOrganization complete."
