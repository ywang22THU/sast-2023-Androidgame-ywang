package de.mide.pegsolitaire.model;

import static de.mide.pegsolitaire.model.PlaceStatusEnum.BLOCKED;
import static de.mide.pegsolitaire.model.PlaceStatusEnum.PEG;
import static de.mide.pegsolitaire.model.PlaceStatusEnum.SPACE;

import de.mide.pegsolitaire.model.PlaceStatusEnum;

public class GameMap {
    public PlaceStatusEnum[][] Map1 =
            {
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED},
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED},
                    {PEG, PEG, PEG, PEG, PEG, PEG, PEG},
                    {PEG, PEG, PEG, SPACE, PEG, PEG, PEG},
                    {PEG, PEG, PEG, PEG, PEG, PEG, PEG},
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED},
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED}
            };

    public PlaceStatusEnum[][] Map2 =
            {
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED},
                    {BLOCKED, PEG, PEG, PEG, PEG, PEG, BLOCKED},
                    {PEG, PEG, PEG, SPACE, PEG, PEG, PEG},
                    {PEG, PEG, PEG, PEG, PEG, PEG, PEG},
                    {PEG, PEG, PEG, PEG, PEG, PEG, PEG},
                    {BLOCKED, PEG, PEG, PEG, PEG, PEG, BLOCKED},
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED}
            };

    public PlaceStatusEnum[][] Map3 =
            {
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED, BLOCKED},
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED, BLOCKED},
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED, BLOCKED},
                    {PEG, PEG, PEG, PEG, PEG, PEG, PEG, PEG},
                    {PEG, PEG, PEG, SPACE, PEG, PEG, PEG, PEG},
                    {PEG, PEG, PEG, PEG, PEG, PEG, PEG, PEG},
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED, BLOCKED},
                    {BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED, BLOCKED},
            };

    public PlaceStatusEnum[][]Map4 =
            {
                {BLOCKED,BLOCKED, BLOCKED, BLOCKED, PEG, BLOCKED, BLOCKED, BLOCKED, BLOCKED},
                {BLOCKED,BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED, BLOCKED},
                {BLOCKED, BLOCKED,PEG, PEG, PEG, PEG, PEG, BLOCKED, BLOCKED},
                {BLOCKED, PEG, PEG, PEG, PEG, PEG, PEG, PEG, BLOCKED},
                {PEG, PEG, PEG, PEG, SPACE, PEG, PEG, PEG, PEG},
                {BLOCKED, PEG, PEG, PEG, PEG, PEG, PEG, PEG, BLOCKED},
                {BLOCKED, BLOCKED,PEG, PEG, PEG, PEG, PEG, BLOCKED, BLOCKED},
                {BLOCKED,BLOCKED, BLOCKED, PEG, PEG, PEG, BLOCKED, BLOCKED, BLOCKED},
                {BLOCKED,BLOCKED, BLOCKED, BLOCKED, PEG, BLOCKED, BLOCKED, BLOCKED, BLOCKED}
            };

    public PlaceStatusEnum[][] Map0 = {
            {PEG,PEG,SPACE},{SPACE,SPACE,SPACE},{SPACE,SPACE,SPACE}
    };
}
