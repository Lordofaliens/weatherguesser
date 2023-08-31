/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'logo': 'rgba(245,245, 245, 1)',
        'second': 'rgba(46, 128, 176, 1)',
        'backBlack':'rgba(0, 0, 0, 0.4)',
        'green':'rgba(171, 219, 185, 1)',
        'yellow':'rgba(253, 237, 155, 1)',
        'red':'rgba(239, 154, 154, 1)'
      },
      fontSize: {
        'base': '20px',
      },
      width: {
        'mainDiv': '550px',
        'leaderboard': '280px',
        'authBtn': '120px',
        'footerIcon': '30px',
        'diffIcon': '30px',
        'guessIcon': '40px',
        'account': '50vw',
        'slider': '30vw',
        'base': '100rem',
      },
      height: {
        'mainDiv': '275px',
        'footerIcon': '30px',
        'diffIcon': '40px'
      },
      minWidth: {
        'account': '20rem'
      },
      maxHeight: {
        'account': '40vh'
      },
      scale: {
        'diffNotActive': '0.8',
        'diffActive': '1'
      },
      transitionDuration: {
        'gradient': '15000'
      },
      animation: {
        'gradientAnimation': 'gradientAnimation 15s ease-in-out infinite',
        'moveLAnimation': 'moveL 2s ease-in-out infinite',
        'moveRAnimation': 'moveR 2s ease-in-out infinite',
        'opacityAnimation': 'opacity 1s ease-in-out',
        'scaleAnimation': 'scale 1s ease-in-out',
        'scaleLargeAnimation': 'scaleLarge 1s ease-in-out',
        'rotateAnimation': 'rotateCircle 2s linear infinite',
        'rotate5Animation': 'rotate5Circle 1s ease-in-out infinite',
        'guessBtnAnimation': 'opacity 1s ease-in-out, scale 1s ease-in-out',
        'guessBackBtnAnimation': 'scale 1s ease-in-out, rotateQuarterCircle 1s ease-in-out',
        'guessedBackBtnAnimation': 'scale 1s ease-in-out, rotateQuarterCircle 1s ease-in-out, rotateCircle 2s linear infinite'
      }
    },
    keyframes: {
      rotate5Circle: {
        '0%': {transform: 'rotate(0deg)'},
        '25%': {transform: 'rotate(-5deg)'},
        '75%': {transform: 'rotate(5deg)'},
        '100%': {transform: 'rotate(0deg)'}
      },
      rotateQuarterCircle: {
        '0%': {transform: 'rotate(-90deg)'},
        '100%': {transform: 'rotate(0deg)'}
      },
      rotateCircle: {
        '0%': { transform: 'rotate(0deg)' },
        '100%': { transform: 'rotate(360deg)' },
      },
      gradientAnimation: {
        '0%': { backgroundPosition: '0% 50%'},
        '50%': { backgroundPosition: '100% 50%'},
        '100%': { backgroundPosition: '0% 50%'}
      },
      scale: {
        '0%': {scale: 0},
        '100%': {scale: 1}
      },
      scaleLarge: {
        '0%': {scale: 0},
        '80%': {scale: 1.1},
        '100%': {scale: 1}
      },
      opacity: {
        '0%': {opacity: 0},
        '100%': {opacity: 1}
      },
      moveL: {
        '0%': {transform: 'translateX(0px)'},
        '50%': {transform: 'translateX(-10px)'},
        '100%': {transform: 'translateX(0px)'}
      },
      moveR: {
        '0%': {transform: 'translateX(0px)'},
        '50%': {transform: 'translateX(10px)'},
        '100%': {transform: 'translateX(0px)'}
      }
    },
  },
  plugins: [],
}