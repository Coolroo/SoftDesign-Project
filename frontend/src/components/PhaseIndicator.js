import React, { Component } from "react";

class PhaseIndicator extends Component{

    namePhase()
    {
        var phase = this.props.state.game.playState
        if (phase === "START_OF_TURN")
        {
            return "Start of Turn"
        }
        else if (phase === "DNA")
        {
            return "DNA Phase"
        }
        else if (phase === "COUNTRY")
        {
            return "Country Phase"
        }
        else if (phase === "EVOLVE")
        {
            return "Evolve Phase"
        }
        else if (phase === "INFECT")
        {
            return "Infect Phase"
        }
        else if (phase === "DEATH")
        {
            return "Death Phase"
        }
        else if (phase === "END_OF_TURN")
        {
            return "End of Turn"
        }
        return this.props.state.game.playState
    }

    getTooltipText()
    {
        var phase = this.props.state.game.playState
        if (phase === "START_OF_TURN")
        {
            return "The turn is just beginning.\nProceed when ready."
        }
        else if (phase === "DNA")
        {
            return "Score 1 DNA point for each Country you control.  [CONTROL means to have most of the Plague Tokens on a Country, including ties].  Points have already been applied.  Proceed when ready."
        }
        else if (phase === "COUNTRY")
        {
            return "Drag a COUNTRY CARD from either the 3 COUNTRY SLOTS or DECK to the BOARD to place it OR to the RED DISCARD PILE to refill your hand to 5 TRAIT CARDS then proceed when ready."
        }
        else if (phase === "EVOLVE")
        {
            return "Drag a TRAIT CARD from your HAND to your PLAGUE CARD to gain a new ability.  You spend DNA POINT [indicated in the top-left of the TRAIT CARD].  You can SKIP this phase.  Proceed after making your choice."
        }
        else if (phase === "INFECT")
        {
            return "Place PLAGUE TOKENS on board COUNTRY CARDS equal to your INFECTIVITY, or until you can't place any more.  You can only place Tokens on countries for which you meet the requirements."
        }
        else if (phase === "DEATH")
        {
            return "If you have PLAGUE TOKENS on any COUNTRY CARDS that are full, roll the DEATH DICE.  If you roll below or equal to your LETHALITY, that country dies.  Then, all players with tokens there draw 1 EVENT CARD."
        }
        else if (phase === "END_OF_TURN")
        {
            return "Your turn has ended.  Proceed when ready."
        }
        return this.props.state.game.playState
    }

    render() {
        return(
            <React.Fragment>{
                    <div>
                        <div className="joinGameButton" style={{marginBottom: "7%", position:"relative", width:"150%", "--color": this.props.state.game.currTurn.toLowerCase()}}>{this.namePhase()}
                            <div className="tooltip">
                                <span class="tooltiptext">{this.getTooltipText()}</span>
                            </div>
                        </div>
                        
                    </div>
                }
            </React.Fragment>
        )
    }
}

export default PhaseIndicator;