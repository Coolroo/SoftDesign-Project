import React, { Component } from "react";
import Dice from 'react-dice-roll';

class CountryCard extends Component{
    state = {
        rollingDice: false,
        diceRef: null
    }

    divRef = React.createRef();

    async afterRoll(val) {
        await new Promise(r => setTimeout(r, 2000));
        this.setState((prevState) => {
            return {
                rollingDice: false,
                diceRef: null
            }
        })
    };

    render() {
        // 2 slot countries:
        // greenland, iceland, libya, panama
        const cardName = this.props.country.countryName;
        
        var countrySlots = [null, 
                            null,
                            //2 slot country 
                            [
                                {top:'22%', left:'16.5%'},
                                {top:'61.5%', left:'66.0%'}
                            ],
                            //3 slot country
                            [
                                {top:'22%', left:'40.5%'},
                                {top:'61.5%', left:'15.5%'},
                                {top:'61.5%', left:'64.0%'}
                            ],
                            //4 slot country
                            [
                                {top:'20%', left:'15.5%'},
                                {top:'20%', left:'66%'},
                                {top:'61.0%', left:'16%'},
                                {top:'60.5%', left:'66.0%'}
                            ],
                            //5 slot country
                            [
                                {top:'20%', left:'24.5%'},
                                {top:'20%', left:'57%'},
                                {top:'60.0%', left:'7%'},
                                {top:'60.0%', left:'40%'},
                                {top:'60.0%', left:'74.0%'}
                            ],
                            //6 slot country
                            [
                                {top:'21%', left:'8%'},
                                {top:'21%', left:'40.5%'},
                                {top:'21%', left:'75%'},
                                {top:'60.5%', left:'7.5%'},
                                {top:'60.5%', left:'40.5%'},
                                {top:'60.5%', left:'74.5%'}
                            ],
                            //7 slot country
                            [
                                {top:'20.5%', left:'25%'},
                                {top:'21%', left:'57%'},
                                {top:'40%', left:'9%'},
                                {top:'40%', left:'72.5%'},
                                {top:'60%', left:'8.5%'},
                                {top:'60%', left:'41.5%'},
                                {top:'60%', left:'72.5%'}
                            ],
                            //8 slot country
                            [
                                {top:'21%', left:'8%'},
                                {top:'21.5%', left:'42%'},
                                {top:'21%', left:'75%'},
                                {top:'41%', left:'7.5%'},
                                {top:'41.5%', left:'74.5%'},
                                {top:'61.%', left:'7%'},
                                {top:'61%', left:'41.5%'},
                                {top:'61%', left:'75%'}
                            ]
        ];
        
        let sides = ["/dice/dice_one.jpg", 
        "/dice/dice_two.jpg", 
        "/dice/dice_three.jpg", 
        "/dice/dice_four.jpg", 
        "/dice/dice_five.jpg", 
        "/dice/dice_six.jpg"]
        
        var hexagons = [];
        const colors = {
            RED: "red",
            BLUE: "blue",
            YELLOW: "yellow",
            PURPLE: "purple",

        }

        for(let i = 0; i < this.props.country.cities.length; i++){
            var color = this.props.country.cities[i];
            var background = color === "EMPTY" ? {} : {backgroundImage: "url(/plague_tokens/token_" + colors[color] + ".png)"};
            hexagons.push(<div style={{...countrySlots[this.props.country.cities.length][i], ...background}}  className="hexagon"/>)
        }
        

        let click = () => {
            switch(this.props.state.game.playState){
                case "INFECT":
                    this.props.infect(this.props.country.countryName);
                    break;
                case "DEATH":
                    this.props.kill(this.props.country.countryName).then(resp => {
                        if(resp.ok){
                            resp.text().then((text) => {
                                console.log("Dice rolled " + text);
                                this.setState((prevState) => {
                                    return {
                                        ...prevState,
                                        rollingDice: true
                                    };
                                }, () => {
                                    this.state.diceRef.rollDice(parseInt(text));
                                });
                            });
                        }
                    });
                    break;
            }
            
        }

        let renderDice = () => {
            if(this.state.rollingDice && this.divRef.current){
                return <Dice faces={sides} size={this.divRef.current.clientHeight * 0.4} ref={(dice) => {this.state.diceRef = dice}} onRoll={(value) => this.afterRoll(value)}/>;
            }
        }

        return(
            <React.Fragment>{
                <div onClick={click} ref={this.divRef}>
                    
                    <img src={`/countries/${cardName}.png`} className="card" alt="img"/>
                    <div style={{position: "absolute", top:"25%", left:"25%", zIndex:"11"}}>{renderDice()}</div>
                    
                    {hexagons}
                </div>
            
            }       
            </React.Fragment>
        )


    }
}

export default CountryCard;