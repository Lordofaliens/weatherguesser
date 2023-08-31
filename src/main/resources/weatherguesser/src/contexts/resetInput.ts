import {styled} from "@mui/material";

export default styled('div')({
    padding: '1px',
    borderRadius: '5px',
    backgroundImage: 'linear-gradient(to left, rgba(245, 245, 245, 1), rgba(45, 130, 178, 1))',
    color: 'white',
    '& .MuiOutlinedInput-input': {
        padding: '4px',
        fontWeight: 'bold',
        width: '280px',
        height: '100%',
        color: 'white',
        textAlign: 'left',
        '&::placeholder': {
            color: 'white',
            textAlign: 'left',
        },
    },
    '& label': {
        fontWeight: 'bold',
        margin: '0',
        padding: '0',
        color: 'transparent'
    },
    '& label.Mui-focused': {
        color: 'rgba(45, 130, 178, 1)'
    },
    '& .MuiInput-underline:after': {
        borderBottomColor: '#B2BAC2',
    },
    '& .MuiOutlinedInput-root': {
        '& fieldset': {
            borderColor: 'white',
        },
        '&:hover fieldset': {
            borderColor: 'rgba(245, 245, 245, 1)',
        },
        '&.Mui-focused fieldset': {
            borderColor: 'rgba(45, 130, 178, 1)',
        },
    },
});