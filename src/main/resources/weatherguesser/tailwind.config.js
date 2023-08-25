/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      fontSize: {
        'base': '20px',
      },
      width: {
        'account': '50vw',
        'base': '100rem',
      },
      minWidth: {
        'account': '20rem'
      },
      maxHeight: {
        'account': '40vh'
      }
    },
  },
  plugins: [],
}